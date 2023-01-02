//
//  CardItem.swift
//  iosApp
//
//  Created by Toby on 1/2/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CardItem: View {
    var card: Card
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(card.name)
                .font(.title3)
                .fontWeight(.semibold)
                .foregroundColor(Color.black)
        }
        .frame(
            maxWidth: .infinity,
            minHeight: 100,
            maxHeight: .infinity,
            alignment: .center
        )
        .background(Color(hex: card.color))
        .clipShape(RoundedRectangle(cornerRadius: 10.0))
    }
}

struct CardItem_Previews: PreviewProvider {
    static var previews: some View {
        CardItem(
            card: Card(id: nil, name: "Costco", image: "", barcode: "123456789", type: 1, color: 0xFF2341, created: DateTimeUtil().now(), notes: "No notes!")
        )
    }
}
