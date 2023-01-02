//
//  CardListViewModel.swift
//  iosApp
//
//  Created by Toby on 1/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension CardListScreen {
    @MainActor class CardListViewModel: ObservableObject {
        private var cardDataSource: CardDataSource? = nil
        
        private let searchCards = SearchCards()
        
        private var cards = [Card]()
        @Published private(set) var filteredCards = [Card]()
        @Published var searchText = "" {
            didSet {
                self.filteredCards = searchCards.execute(cards: self.cards, query: searchText)
            }
        }
        @Published private(set) var isSearchActive = false
        
        init(cardDataSource: CardDataSource? = nil) {
            self.cardDataSource = cardDataSource
        }
        
        func addData() {
            cardDataSource?.insertCard(card: Card(id: nil, name: "Costco", image: "", barcode: "123456789", type: 1, color: 0xFF2341, created: DateTimeUtil().now(), notes: "No notes!"), completionHandler: { error in })
            cardDataSource?.insertCard(card: Card(id: nil, name: "Walmart", image: "", barcode: "4324234421", type: 1, color: 0xFF6262, created: DateTimeUtil().now(), notes: "No notes!"), completionHandler: { error in })
            self.loadCards()
        }
        
        func loadCards() {
            cardDataSource?.getAllCards(completionHandler: { cards, error in
                self.cards = cards ?? []
                self.filteredCards = self.cards
            })
        }
        
        func deleteCardById(id: Int64?) {
            if id != nil {
                cardDataSource?.deleteCardById(id: id!, completionHandler: { error in
                    self.loadCards()
                })
            }
        }
        
        func toggleIsSearchActive() {
            isSearchActive = !isSearchActive
            
            if !isSearchActive {
                searchText = ""
            }
        }
        
        func setCardDataSource(cardDataSource: CardDataSource) {
            self.cardDataSource = cardDataSource
            loadCards()
            //addData()
        }
    }
}
