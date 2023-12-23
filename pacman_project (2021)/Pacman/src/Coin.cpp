#include "GameObjectStruct.h"
#include "UI.h"
#include "Coin.h"

#include <SDL.h>

#include <map>
#include <vector>

bool Coins::hasCollided(int x, int y) { 
    if (coinMap[y][x] == 2) {
        coinMap[y][x] = 3;
        return true;
    } 
    else {
        return false;
    }
}

void Coins::setCoins(int x, int y) { 
    coinMap[y][x] = 2;
}
int Coins::getCoins(int x, int y) { return coinMap[y][x]; }
