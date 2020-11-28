//
//  HighScoreView.swift
//  Arcade Hoops
//
//  Created by Samuel Donovan on 11/11/20.
//

import SwiftUI

struct HighScoreView: View {
    
    @ObservedObject var store = highScoreStore
    
    var body: some View {
        VStack {
            Text("LEADERBOARD")
                .foregroundColor(.black)
                .font(.largeTitle)
                .padding(30)
                .background(RoundedRectangle(cornerRadius: 20.0).foregroundColor(Color("Color1")))
            if store.failedUpdate {
                Text("Could not update leaderboard")
                    .italic()
                    .foregroundColor(.red)
            }
            Spacer()
            Divider()
            Spacer()
            List(0..<10, rowContent: {(num: Int) in
                if store.scores.count <= num {
                    EmptyView()
                } else {
                    HStack {
                        Text("\(num + 1). \(store.scores[num].username)")
                        Spacer()
                        Text(store.scores[num].score.description)
                            .bold()
                    }
                }
            })
            Spacer()
            Button(action: {
                toView(view: LoggedInView())
            }) {
                Text("Return to Main Menu")
                    .foregroundColor(.black)
                    .padding(10)
                    .background(RoundedRectangle(cornerRadius: 10.0).foregroundColor(Color("Color")))}
            .buttonStyle(PlainButtonStyle())
            .padding()
        }
    }
}

struct HighScoreView_Previews: PreviewProvider {
    static var previews: some View {
        HighScoreView()
    }
}
