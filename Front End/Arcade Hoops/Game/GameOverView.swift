//
//  ContentView.swift
//  GameOverScreen
//
//  Created by Samuel Donovan on 11/11/20.
//

import SwiftUI

struct GameOverView: View {
    
    
    let userName = currentUser ?? "GUEST"
    let score: Int
    
    @State var offset: CGFloat = 0.0
    
    
    var body: some View {
        ZStack {
            Color("Color")
                .edgesIgnoringSafeArea(.all)
            VStack {
                Spacer()
                Text("Congratulations,\n\(userName)!\n\nYou made \(score) hoops!")
                    .font(.largeTitle)
                    .fontWeight(.black)
                    .foregroundColor(Color("Color1"))
                    .multilineTextAlignment(.center)
                    .padding()
                    .offset(x: 0.0, y: -30.0 + self.offset)
                    .animation(Animation.easeInOut(duration: 2.0).repeatForever(autoreverses: true))
                    .onAppear {
                        self.offset = 60.0
                    }
           Spacer()
                Divider()
                Button(action: {
                    toView(view: LoggedInView())
                }, label: {
                    Text("To Main Menu")
                        .font(.title3)
                        .bold()
                })
                .foregroundColor(.black)
                .padding(30)
                .background(RoundedRectangle(cornerRadius: 20.0).stroke().foregroundColor(.black))
                .background(RoundedRectangle(cornerRadius: 20.0).foregroundColor(Color("Color1")))
                .padding(30)
              
            }
            
            
        }
        
    }
}
