//
//  LoginView.swift
//  Arcade Hoops
//
//  Created by Samuel Donovan on 11/10/20.
//

import SwiftUI

import SwiftUI

struct ContentView: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LoginViewStack()
        }
    }
}

struct LoginViewStack : View {
    
    @State var loggingIn = true
    
    @State var userName: String = ""
    @State var password: String = ""
    
    @State var playAsGuest: Bool = false
    
    var body : some View {
        
        VStack {
            UserImage()
            
            VStack(spacing: 4) {
                HStack(spacing: 0) {
                    Text("Arcade ")
                        .font(.system(size: 30, weight: .heavy))
                        .foregroundColor(Color("Color"))
                    Text("Hoops")
                        .font(.system(size: 30, weight: .heavy))
                        .foregroundColor(Color("Color1"))
                }
            }
            
            HStack {
                Button(action: {
                    withAnimation(.spring(response: 0.8, dampingFraction: 0.5, blendDuration: 0.5)) {
                        loggingIn = true
                    }
                }) {
                    Text("Existing")
                        .foregroundColor(loggingIn ? .white : .black)
                        .fontWeight(.bold)
                        .padding(.vertical, 10)
                        .frame(width: (UIScreen.main.bounds.width - 50) / 2)
                }
                .background(loggingIn ? Color("Color") : Color.clear)
                .clipShape(Capsule())
                
                Button(action: {
                    withAnimation(.spring(response: 0.8, dampingFraction: 0.5, blendDuration: 0.5)){
                        loggingIn = false
                    }
                }) {
                    Text("New")
                        .foregroundColor(loggingIn ? .black : .white)
                        .fontWeight(.bold)
                        .padding(.vertical, 10)
                        .frame(width: (UIScreen.main.bounds.width - 50) / 2)
                }
                .background(!loggingIn ? Color("Color") : Color.clear)
                .clipShape(Capsule())
            }
            .background(Color.gray)
            .clipShape(Capsule())
            .padding(.top, 20)
            
            if loggingIn {
                LoginView()
            } else {
                SignUpView()
            }
            
            HStack(spacing: 15) {
                Color.white.opacity(0.7)
                    .frame(width: 35, height: 1)
                
                Text("or")
                    .fontWeight(.bold)
                    .foregroundColor(Color("Color1"))
                
                Color.white.opacity(0.7)
                    .frame(width: 30, height: 1)
            }
            .padding(.top, 5)
            
            HStack{
                Button(action: {
                    toView(view: LoggedInView())
                }) {
                    Text("PLAY AS A GUEST")
                        .foregroundColor(.white)
                        .fontWeight(.bold)
                        .padding(.vertical)
                        .frame(width: UIScreen.main.bounds.width - 100)
                }
                .background(Color.gray)
                .cornerRadius(8)
                .offset(y: -40)
            }
            .padding(.top, 40)
        }
    }
}

struct UserImage: View {
    var body: some View {
        Image("userImage")
            .resizable()
            .scaledToFill()
    }
}

class IdentifiableString: Identifiable {
    let string: String
    init(string: String) {
        self.string = string
    }
}

struct LoginView : View {
    @State var failMessage: IdentifiableString?

    @State var userName: String = ""
    @State var password: String = ""
    
    var body : some View{
        
        VStack {
            VStack {
                HStack(spacing: 15) {
                    
                    Image(systemName: "person")
                        .foregroundColor(.black)
                    
                    TextField("Username", text: self.$userName)
                        .foregroundColor(.black)
                    
                }
                .padding(.vertical, 20)
                
                Divider()
                
                HStack(spacing: 15) {
                    Image(systemName: "lock")
                        .resizable()
                        .frame(width: 15, height: 18)
                        .foregroundColor(.black)
                    
                    SecureField("Password", text: self.$password)
                        .foregroundColor(.black)
                }
                .padding(.vertical, 20)
            }
            .padding(.vertical)
            .padding(.horizontal, 20)
            .padding(.bottom, 40)
            .background(Color.white)
            .cornerRadius(10)
            .padding(.top, 25)
            
            Button(action: {
                
                if userName == "" || password == "" {
                    failMessage = IdentifiableString(string: "No blanks allowed!")
                    return
                }
//                if userName == "samshoota" && password == "password" {
//                    currentUser = "samshoota"
//                    toView(view: LoggedInView())
//                } else {
//                    failMessage = IdentifiableString(string: "User does not exist!")
//                }
                
//                fatalError("Delete most of above when ready")
                
                CloudCommunicator.attemptLogin(username: userName, password: password, goodLogin: {
                    currentUser = userName
                    toView(view: LoggedInView())
                }, networkFailure: {
                    failMessage = .init(string: "Bad Connection")
                }, badLogin: {failMessage = .init(string: "Bad Login")})
            }) {
                
                Text("LOGIN")
                    .foregroundColor(.white)
                    .fontWeight(.bold)
                    .padding(.vertical)
                    .frame(width: UIScreen.main.bounds.width - 100)
                
            }.background(
                Color("Color")
            )
            .cornerRadius(8)
            .offset(y: -40)
            .padding(.bottom, -40)
        }
        .alert(item: $failMessage, content: {(message: IdentifiableString) in
            Alert(title: Text("Login Error").foregroundColor(.red), message: Text(message.string), dismissButton: .cancel())
        })
    }
}


struct SignUpView : View {
    
    @State var failMessage: IdentifiableString?
    
    @State var userName: String = ""
    @State var password: String = ""
    
    var body : some View {
        VStack {
            VStack {
                HStack(spacing: 15) {
                    Image(systemName: "person")
                        .foregroundColor(.black)
                    TextField("Username", text: self.$userName)
                        .foregroundColor(.black)
                }
                .padding(.vertical, 20)
                
                Divider()
                
                HStack(spacing: 15) {
                    Image(systemName: "lock")
                        .resizable()
                        .frame(width: 15, height: 18)
                        .foregroundColor(.black)
                    SecureField("Password", text: self.$password)
                        .foregroundColor(.black)
                    
                }
                .padding(.vertical, 20)
            }
            .padding(.vertical)
            .padding(.horizontal, 20)
            .padding(.bottom, 40)
            .background(Color.white)
            .cornerRadius(10)
            .padding(.top, 25)
            
            Button(action: {
                if userName == "" || password == ""{
                    failMessage = .init(string: "No blanks allowed!")
                    return
                }
                
                currentUser = userName
//                return;
//                fatalError("DELETE 2 LINEs ABOVE WHEN READY")
                
                CloudCommunicator.createUser(username: userName, password: password, networkFailure: {
                    failMessage = .init(string: "Bad Connection")
                }, badAttempt: {
                    failMessage = .init(string: "Username Taken")
                }, goodAttempt: {
                    currentUser = userName
                    toView(view: LoggedInView())
                })
            }) {
                Text("SIGN UP")
                    .foregroundColor(.white)
                    .fontWeight(.bold)
                    .padding(.vertical)
                    .frame(width: UIScreen.main.bounds.width - 100)
            }.background(
                Color("Color")
            )
            .cornerRadius(8)
            .offset(y: -40)
            .padding(.bottom, -40)
            //            .shadow(radius: 15)
        }
        .alert(item: $failMessage, content: {(message: IdentifiableString) in
            Alert(title: Text("Sign Up Error").foregroundColor(.red), message: Text(message.string), dismissButton: .cancel())
        })
    }
}
