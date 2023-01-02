//
//  CardListScreen.swift
//  iosApp
//
//  Created by Toby on 1/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CardListScreen: View {
    private var cardDataSource: CardDataSource
    @StateObject var viewModel = CardListViewModel(cardDataSource: nil)
    
    private let columns: [GridItem] = [
        GridItem(.adaptive(minimum: 150))
    ]
    
    init(cardDataSource: CardDataSource) {
        self.cardDataSource = cardDataSource
    }
    
    var body: some View {
        VStack {
            ZStack {
                HideableSearchTextField<EmptyView>(
                    onSearchToggled: {
                        viewModel.toggleIsSearchActive()
                    },
                    destinationProvider: {
                        EmptyView()
                    },
                    isSearchActive: viewModel.isSearchActive,
                    searchText: $viewModel.searchText)
                .frame(maxWidth: .infinity, minHeight: 40)
                .padding()
                
                if !viewModel.isSearchActive {
                    Text("My Cards")
                        .font(.title2)
                }
            }
            ScrollView {
                LazyVGrid(columns: columns) {
                    ForEach(viewModel.filteredCards, id: \.self.id) { card in
                        Button(action: {
                            //viewModel.addData()
                        }) {
                            CardItem(card: card)
                        }
                    }
                }
                .padding()
            }
            .onAppear {
                viewModel.loadCards()
            }
        }
        .onAppear {
            viewModel.setCardDataSource(cardDataSource: cardDataSource)
        }
    }
}

struct CardListScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}
