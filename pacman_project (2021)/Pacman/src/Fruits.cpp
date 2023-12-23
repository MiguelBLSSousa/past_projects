#include "GameObjectStruct.h"
#include "UI.h"
#include "Fruits.h"

#include <SDL.h>

#include <map>
#include <vector>

bool Fruits::hasCollided(int x, int y) {
    if (fruitMap[y][x] == 2) {
        fruitMap[y][x] = 3;
        return true;
    } else {
        return false;
    }
}
void Fruits::setFruit(int x, int y)
{ 
    fruitMap[y][x] = 2;
}
int Fruits::getFruit(int x, int y) { return fruitMap[y][x]; }
Type Fruits::getFruitType(int fruitChoice) { 
    if (fruitChoice == 1) {
        return APPLE;
    } 
    else if (fruitChoice == 2) {
        return LEMON;
    } else if (fruitChoice == 3) {
        return STRAWBERRY;
    } else if (fruitChoice == 4) {
        return CHERRY;
    } else if (fruitChoice == 5) {
        return ORANGE;
    } else if (fruitChoice == 6) {
        return GRAPES;
    }
}
void Fruits::setCap(int new_cap) { cap = new_cap; }
int Fruits::getCap() { return cap; }