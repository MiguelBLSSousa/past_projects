#include "GameObjectStruct.h"
#include "UI.h"
#include "Powerup.h"

#include <SDL.h>

#include <map>
#include <vector>

int Powerup::getXpos() { return x_pos; }
void Powerup::setXpos(int new_x_pos) { x_pos = energiser.x = new_x_pos; }
int Powerup::getYpos() { return y_pos; }
void Powerup::setYpos(int new_y_pos) { y_pos = energiser.y = new_y_pos; }
bool Powerup::hasCollided(int x, int y) { 
	if (x == this->x_pos && y == this->y_pos) {
        return true;
    } else {
        return false;
    }
}
void Powerup::setObject(int x, int y) { 
	energiser.type = ENERGIZER; 
    this->setXpos(x);
    this->setYpos(y);
    energiser.dir = UP;
}
GameObjectStruct Powerup::getObject() { return energiser; }