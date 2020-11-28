//
//  ContentView.swift
//  Arcade Hoops
//
//  Created by Samuel Donovan on 10/15/20.
//

import SwiftUI
import RealityKit

struct GameView : View {
    
    @ObservedObject var gameManager = GameManager()
    
    var body: some View {
        ZStack {
            ARViewContainer(arView: gameManager.arView)
                .edgesIgnoringSafeArea(.all)
            GeometryReader {(proxy: GeometryProxy) in
                VStack {
                    HStack  {
                        VStack {
                            Text("Score: \(gameManager.madeCount)")
                            Text("\(gameManager.time) second\(gameManager.time == 1 ? "" : "s") left")
                                .italic()
                        }
                        .padding()
                        Spacer()
                        Button(action: {
                            if gameManager.playing {
                                gameManager.pause()
                            } else {
                                gameManager.play()
                            }
                        })  {
                            Image(systemName: "\(gameManager.playing ? "pause" : "play").rectangle.fill")
                                .font(.title)
                        }
                        .padding()
                    }
                    .background(RoundedRectangle(cornerRadius: 20.0).foregroundColor(Color(.sRGB, white: 0.2, opacity: 0.3)))
                    .padding()
                    Spacer()
                    Button(action: {
                        gameManager.shoot()
                    }) {
                        ZStack {
                            Circle()
                                .foregroundColor(Color("Color1"))
                            Circle()
                                .stroke()
                                .foregroundColor(.black)
                            Text("SHOOT")
                                .font(.title2)
                                .foregroundColor(.gray)
                        }
                        .frame(width: proxy.size.width/3, height: proxy.size.width/3, alignment: .center)
                        .padding()
                    }
                    .disabled(!gameManager.playing)
                }
            }
        }
    }
}

struct ARViewContainer: UIViewRepresentable {
    
    let arView: ARView
    
    func makeUIView(context: Context) -> ARView {
        return arView
    }
    
    func updateUIView(_ uiView: ARView, context: Context) {}
    
}
