//
//  GameManager.swift
//  Arcade Hoops
//
//  Created by Samuel Donovan on 11/7/20.
//

import Foundation
import ARKit
import RealityKit
import Combine

let boxAnchor = try! Experience.loadBox()

class GameManager: ObservableObject {
    
    @Published var madeCount = 0
    @Published var time: Int
    @Published var playing = false
    
    let arView: ARView
    var sceneUpdate: AnyCancellable! = nil
    var collisionHappened: AnyCancellable! = nil
    var goToBackground: AnyCancellable! = nil
    
    var currentTime = 0.0
    let maxTime = 45.0
    
    enum BallState {
        case inHands
        case inAir
        case hasCollided
    }
    
    var ballState: BallState = .inHands
    
    let ballPrefab: HasPhysics
    var ball: HasPhysics
    var balls = [HasPhysics]()
    let player: Entity
    
    let hoopTrigger: TriggerVolume
    let groundTrigger: TriggerVolume
    
    let forever: AudioPlaybackController
    
    func pause() {
        forever.pause()
        playing = false
    }
    
    func play() {
        forever.play()
        playing = true
    }
    
    init() {
        arView = ARView(frame: .zero, cameraMode: .ar, automaticallyConfigureSession: true)
        
        arView.scene.addAnchor(boxAnchor)
        forever = boxAnchor.prepareAudio(try! AudioFileResource.load(named: "Forever.m4a"))
        
        ballPrefab = boxAnchor.ball as! HasPhysics
        ballPrefab.physicsBody!.massProperties.mass = 1.0
        ballPrefab.physicsBody!.mode = .kinematic
        ballPrefab.removeFromParent()
        
        ball = ballPrefab.clone(recursive: true)
        boxAnchor.addChild(ball)
        
        player = boxAnchor.player!
        
        player.transform.translation = [0.3,0.0,0.246]
        ball.transform.translation = [0.3,0.245,0.1754]
        
        hoopTrigger = TriggerVolume(shape: ShapeResource.generateBox(size: [0.1,0.01,0.1]), filter: .default)
        boxAnchor.addChild(hoopTrigger)
        hoopTrigger.transform.translation = [0.0,0.36,-0.25]
        
        groundTrigger = TriggerVolume(shape: ShapeResource.generateBox(size: [3.0,0.03,3.0]), filter: .default)
        boxAnchor.addChild(groundTrigger)
        groundTrigger.transform.translation = [0.0,0.01,-0.25]
        
        time = Int(maxTime)
        
        sceneUpdate = arView.scene.publisher(for: SceneEvents.Update.self).sink(receiveValue: {[unowned self] in everyFrame(frameUpdate:$0)})
        collisionHappened = arView.scene.publisher(for: CollisionEvents.Began.self).sink(receiveValue: {[unowned self] in collisionEventBegan(collisionEvent: $0)})
        
        goToBackground = NotificationCenter.default.publisher(for: UIScene.willDeactivateNotification).sink(receiveValue: {[unowned self] _ in
            self.pause()
        })
    }
    
    deinit {
        boxAnchor.removeChild(hoopTrigger)
        boxAnchor.removeChild(groundTrigger)
        for ball in balls {
            ball.removeFromParent()
        }
        ball.removeFromParent()
        arView.scene.removeAnchor(boxAnchor)
        forever.stop()
    }
    
    func collisionEventBegan(collisionEvent: CollisionEvents.Began) {
        let a = collisionEvent.entityA
        let b = collisionEvent.entityB
        
        if (a == groundTrigger || b == groundTrigger) && (a == ball || b == ball) {
            ballState = .inHands
            balls.append(ball)
            ball = ballPrefab.clone(recursive: true)
            boxAnchor.addChild(ball)
        }
        if ballState == .inAir {
            if (a == hoopTrigger || b == hoopTrigger) && (a == ball || b == ball) {
                print("Bucket")
                madeCount += 1
                ballState = .hasCollided
                return
            }
        }
    }
    
    func everyFrame(frameUpdate: Scene.Publisher<SceneEvents.Update>.Output) {
        let dt = frameUpdate.deltaTime
        
        if playing {
            everyPlayingFrame(dt: dt)
        }
    }
    
    func gameOver() {
        
        if let user = currentUser {
            if highScoreStore.scores.count < 10 || madeCount > highScoreStore.scores.last!.score {
                CloudCommunicator.postScore(username: user, score: madeCount, anyFailure: {})
                highScoreStore.scores.append(.init(score: madeCount, username: user))
                highScoreStore.scores.sort(by:{(score1: UserScore,score2: UserScore) in score1.score > score2.score})
            }
        }
        toView(view: GameOverView(score: madeCount))
    }
    
    func everyPlayingFrame(dt: Double) {
        
        currentTime += dt
        
        if currentTime > maxTime {
            gameOver()
        }
        
        let currentTimeDiscrete = Int(maxTime) - Int(currentTime)
        if currentTimeDiscrete != time {
            time = currentTimeDiscrete
        }
        
        let startingBallTranslation = [0.0,0.245,0.1754] as SIMD3<Float>
        let startingPlayerTranslation = [0.0,0.0,0.246] as SIMD3<Float>
        let max = [0.3,0.0,0.0] as SIMD3<Float>
        let period = 7.0 as Float
        
        let sideToSide = cos(pow(Float(currentTime),1.4)*(2*Float.pi/period))*max
        
        let currentPlayerTranslation = startingPlayerTranslation + sideToSide
        
        player.transform.translation = currentPlayerTranslation
        
        // deal with ball
        
        switch ballState {
        case .inAir, .hasCollided: break
        // ball.applyLinearImpulse(Float(dt)*6*0.035*[0.0,-9.8,0.0], relativeTo: boxAnchor)
        case .inHands:
            ball.transform.translation = startingBallTranslation + sideToSide
        }
        
        
    }
    
    func shoot() {
        if ballState != .inHands {return}
        print("Shot")
        
        ballState = .inAir
        ball.physicsBody!.mode = .dynamic
        
        let cos70: Float = 0.34202014332
        let sin70: Float = 0.93969262078
        let velocity: Float = 1.3
        
        let shootVector: SIMD3<Float> = velocity*[0.0,sin70,-cos70]
        
        // ball.physicsMotion!.linearVelocity = shootVector
        ball.applyLinearImpulse(shootVector, relativeTo: boxAnchor)
        
    }
    
    
}
