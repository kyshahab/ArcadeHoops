//
//  LoggedInView.swift
//  Arcade Hoops
//
//  Created by Samuel Donovan on 11/11/20.
//

import SwiftUI

struct LoggedInView : View {
    var body: some View{
        NavigationView {
            VStack{
                
                VStack(spacing: 0){
                    Text("Welcome to ")
                        .font(.system(size: 40, weight: .heavy))
                        .foregroundColor(Color("Text"))
                    HStack{
                        Text("Arcade ")
                            .font(.system(size: 40, weight: .heavy))
                            .foregroundColor(Color("Color"))
                        
                        Text("Hoops!")
                            .font(.system(size: 40, weight: .heavy))
                            .foregroundColor(Color("Color1"))}
                }
                
                Button(action: {
                    toView(view: GameView())
                }) {
                    
                    Text("PLAY GAME")
                        .foregroundColor(.white)
                        .fontWeight(.bold)
                        .padding(.vertical)
                        .frame(width: UIScreen.main.bounds.width - 100)
                }
                .background(Color("Color"))
                .cornerRadius(8)
                .padding(.top, 25)
                
                Button(action: {
                    highScoreStore.attemptUpdate()
                    toView(view: HighScoreView())
                }) {
                    Text("VIEW LEADERBOARD")
                        .foregroundColor(.white)
                        .fontWeight(.bold)
                        .padding(.vertical)
                        .frame(width: UIScreen.main.bounds.width - 100)
                }
                .background(Color("Color"))
                .cornerRadius(8)
                .padding(.top, 10)
                
            }
            .padding(.bottom,40)
        }
    }
}
