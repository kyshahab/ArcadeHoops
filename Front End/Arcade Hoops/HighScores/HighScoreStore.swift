//
//  HighScoreStore.swift
//  Arcade Hoops
//
//  Created by Samuel Donovan on 11/10/20.
//

import Foundation

let highScoreStore = HighScoreStore()

class HighScoreStore: ObservableObject {
    @Published var failedUpdate = false
    @Published var scores: [UserScore] = [
        .init(score: 100, username: "samshoota"),
        .init(score: 10, username: "player0"),
        .init(score: 8, username: "player1"),
        .init(score: 1, username: "player2"),
    ]
    
    func attemptUpdate() {
        CloudCommunicator.getHighScores(uponSuccess: {self.scores = $0; self.failedUpdate = false;}, networkFailure: {self.failedUpdate = true})
        scores.sort(by:{(score1: UserScore,score2: UserScore) in score1.score > score2.score})
    }
}
