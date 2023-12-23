#include "Pacman.h"
#include "GameObjectStruct.h"
#include "UI.h"
#include <iostream>

#include <map>
#include <string>
#include <vector>

int Pacman::getXpos() { return x_pos; }
void Pacman::setXpos(int new_x_pos) { x_pos = pacman.x = new_x_pos; }
int Pacman::getYpos() { return y_pos; }
void Pacman::setYpos(int new_y_pos) { y_pos = pacman.y = new_y_pos; }

int Pacman::getLifes() { 
    return lifes; 
}
void Pacman::setLifes(int newLifes) { lifes = newLifes; }

int Pacman::getPoints() { return points; }
void Pacman::setPoints(int new_points) { points = new_points; }

void Pacman::setDirection(Direction newDirection) { direction = pacman.dir = newDirection; }
Direction Pacman::getDirection() { return direction; }

void Pacman::move(Direction direction, int x, int y, std::vector<std::vector<int>> &map)
{
    if (direction == UP && map[y + 1][x] == 0) {
        this->setYpos(y + 1);
        this->setDirection(DOWN);
    } else if (direction == DOWN && map[y - 1][x] == 0) {
        this->setYpos(y - 1);
        this->setDirection(UP);
    } else if (direction == RIGHT && map[y][x + 1] == 0) {
        this->setXpos(x + 1);
        this->setDirection(RIGHT);
    } else if (direction == LEFT && map[y][x - 1] == 0) {
        this->setXpos(x - 1);
        this->setDirection(LEFT);
    }
}
void Pacman::setPower(bool power) { isPowered = power; }
bool Pacman::hasPower() { return isPowered; }
void Pacman::respawn() {}
bool Pacman::hasDied() { return isDead; }
void Pacman::setPowerCounter() { powercounter++; }
void Pacman::resetPowerCounter() { powercounter = 0; }
int Pacman::getPowerCounter() { return powercounter; }
void Pacman::setObject() {
    pacman.type = PACMAN;
    pacman.dir = RIGHT;
    pacman.x = 13;
    pacman.y = 5;
    this->setXpos(pacman.x);
    this->setYpos(pacman.y);
}
GameObjectStruct Pacman::getObject() { 
    return pacman; 
}