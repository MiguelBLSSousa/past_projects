#include "Ghost.h"
#include "GameObjectStruct.h"
#include "UI.h"

#include <SDL.h>

#include <map>
#include <vector>
#include <string>


int Ghost::getXpos() { 
    return x_pos; 
}
void Ghost::setXpos(int new_x_pos) { 
    x_pos = ghost.x = new_x_pos; 
}
int Ghost::getYpos() { 
    return y_pos; 
}
void Ghost::setYpos(int new_y_pos) { 
    y_pos = ghost.y = new_y_pos; 
}
void Ghost::setDirection(Direction newDirection) { 
    direction = ghost.dir = newDirection; 
}
Direction Ghost::getDirection() { 
    return direction; 
}
void Ghost::move(Direction direction, int x, int y, std::vector<std::vector<int>> &map)
{

    int counter = 0;
    int paths_x[4], paths_y[4];

    if (x == 0 && y == 13 && direction == LEFT) {
        this->setXpos(27);
        this->setYpos(13);
    } 
    else if (x == 27 && y == 13 && direction == RIGHT) {
        this->setXpos(0);
        this->setYpos(13);
    } 
    else {
        if (x != 27) {
            if (map[y][x + 1] == 0) {
                paths_x[counter] = x + 1;
                paths_y[counter] = y;
                counter++;
            }
        }
        if (x != 0) {
            if (map[y][x - 1] == 0) {
                paths_x[counter] = x - 1;
                paths_y[counter] = y;
                counter++;
            }
        }
        if (map[y + 1][x] == 0) {
            paths_x[counter] = x;
            paths_y[counter] = y + 1;
            counter++;
        }
        if (map[y - 1][x] == 0) {
            paths_x[counter] = x;
            paths_y[counter] = y - 1;
            counter++;
        }
        int random = rand() % counter;

        if (random == 0) {
            this->setDirection(RIGHT);
        }
        if (random == 1) {
            this->setDirection(LEFT);
        }
        if (random == 2) {
            this->setDirection(DOWN);
        }
        if (random == 3) {
            this->setDirection(UP);
        }

        this->setXpos(paths_x[random]);
        this->setYpos(paths_y[random]);
    }
}
bool Ghost::hasCollided(int x_p, int y_p, int x, int y) { 
    if (x_p == x && y_p == y) {
        return true;
    } 
    else {
        return false;
    }
}
void Ghost::setScared(bool new_scared) { 

    if (new_scared == true) {
        ghost.type = SCARED;
        scared = new_scared;
    } 
    else if (new_scared == false) {
        scared = new_scared;
        if (name == "inky") {
            ghost.type = INKY;
        } 
        else if (name == "pinky") {
            ghost.type = PINKY;
        } 
        else if (name == "blinky") {
            ghost.type = BLINKY;
        } 
        else if (name == "clyde") {
            ghost.type = CLYDE;
        }
    }
}
bool Ghost::isScared() { return scared; }
void Ghost::setObject(int ghostType) {
    
    if (ghostType == 1) {
        name = "inky";
        ghost.type = INKY;
        this->setXpos(12);
        this->setYpos(13);
        ghost.dir = UP;
    }
    else if (ghostType == 2) {
        name = "pinky";
        ghost.type = PINKY;
        this->setXpos(13);
        this->setYpos(13);
        ghost.dir = UP;
    }
    else if (ghostType == 3) {
        name = "blinky";
        ghost.type = BLINKY;
        this->setXpos(14);
        this->setYpos(13);
        ghost.dir = UP;
    }
    else if (ghostType == 4) {
        name = "clyde";
        ghost.type = CLYDE;
        this->setXpos(15);
        this->setYpos(13);
        ghost.dir = UP;
    }
}
GameObjectStruct Ghost::getObject() { 
    return ghost; 
}