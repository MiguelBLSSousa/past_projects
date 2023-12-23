/// \file
/// main.cpp

/*
 *  Created on: Jan 29, 2015
 *      Author:
 *       Group:
 */

#include "GameObjectStruct.h"
#include "Ghost.h"
#include "Pacman.h"
#include "UI.h"
#include "Coin.h"
#include "Powerup.h"
#include "Fruits.h"

#include <SDL.h>
#include <vector>
#include <iostream>

/// Callback function to update the game state.
///
/// This function is called by an SDL timer at regular intervals.
/// Note that this callback may happen on a different thread than the main
/// thread. You therefore have to be careful in avoiding data races. For
/// example, you should use mutexes to access shared data. Read the
/// documentation of SDL_AddTimer for more information and for tips regarding
/// multithreading issues.
Uint32 gameUpdate(Uint32 interval, void * /*param*/)
{
    // Do game loop update here
    return interval;
}

/// Program entry point.
int main(int /*argc*/, char ** /*argv*/)
{
    std::vector<std::vector<int>> map = {
        #include "board.def"
    };

    // Create a new ui object
    UI ui(map); // <-- use map from your game objects.

    // Start timer for game update, call this function every 100 ms.
    SDL_TimerID timer_id =
        SDL_AddTimer(50, gameUpdate, static_cast<void *>(nullptr));

    
    Pacman pacman;
    Ghost inky, pinky, clyde, blinky;
    Coins coins;
    Powerup first, second, third, forth;
    Fruits fruits;
   
    std::vector<GameObjectStruct> vcoins;
    std::vector<GameObjectStruct> vpowerups;
    std::vector<GameObjectStruct> vfruits;
    
    for (int i = 0; i < map[0].size(); i++) {
        for (int j = 0; j < map.size(); j++) {
            if (map[j][i] == 0) {
                if (coins.getCoins(i, j) == 0) {
                    GameObjectStruct coin;
                    coin.type = DOT;
                    coin.dir = UP;
                    coin.x = i;
                    coin.y = j;
                    vcoins.push_back(coin);
                    coins.setCoins(i, j);
                }
            }
        }
    }

    pacman.setObject();
    inky.setObject(1);
    pinky.setObject(2);
    blinky.setObject(3);
    clyde.setObject(4);
    first.setObject(1, 1);
    second.setObject(1, 25);
    third.setObject(26, 25);
    forth.setObject(26, 1);

    vpowerups.push_back(first.getObject());
    vpowerups.push_back(second.getObject());
    vpowerups.push_back(third.getObject());
    vpowerups.push_back(forth.getObject());

    // Call game init code here

    bool quit = false;
    while (!quit) {
        // set timeout to limit frame rate
        Uint32 timeout = SDL_GetTicks() + 200;

        // Handle the input
        SDL_Event e;
        while (SDL_PollEvent(&e)) {
            // Quit button.
            if (e.type == SDL_QUIT) {
                quit = true;
            }

            // All keydown events
            if (e.type == SDL_KEYDOWN) {
                switch (e.key.keysym.sym) {
                case SDLK_LEFT: // YOUR CODE HERE
                    pacman.move(LEFT, pacman.getXpos(), pacman.getYpos(), map);
                    break;
                case SDLK_RIGHT: // YOUR CODE HERE
                    pacman.move(RIGHT, pacman.getXpos(), pacman.getYpos(), map);
                    break;
                case SDLK_UP: // YOUR CODE HERE
                    pacman.move(DOWN, pacman.getXpos(), pacman.getYpos(), map);
                    break;
                case SDLK_DOWN: // YOUR CODE HERE
                    pacman.move(UP, pacman.getXpos(), pacman.getYpos(), map);
                    break;
                case SDLK_ESCAPE:
                    break;
                }
            }
        }

        bool breakIT = false;
        if ((pacman.getPoints() % 250 == 0 || pacman.getPoints() % 250 == 5) && pacman.getPoints() != fruits.getCap()) {
            int fruitChoice = 1 + (rand() % 6);
            for (int i = 0; i < map[0].size(); i++) {
                for (int j = 0; j < map.size(); j++) {
                    int random = rand() % 100;
                    if (map[j][i] == 0 && random == 69 && fruits.getFruit(i, j) == 0) {
                            GameObjectStruct fruit;
                            fruit.type = fruits.getFruitType(fruitChoice);
                            fruit.dir = UP;
                            fruit.x = i;
                            fruit.y = j;
                            vfruits.push_back(fruit);
                            fruits.setFruit(j, i);
                            fruits.setCap(pacman.getPoints());
                            breakIT = true;
                            break;
                        }
                    }
                if (breakIT == true) {
                    breakIT = false;
                    break;
                }
            }
        }
        if (fruits.hasCollided(pacman.getXpos(), pacman.getYpos())) {
            pacman.setPoints(pacman.getPoints() + 25);
            for (int i = 0; i < vfruits.size(); i++) {
                if (vfruits[i].x == pacman.getXpos() &&
                    vfruits[i].y == pacman.getYpos()) {
                    vfruits.erase(vfruits.begin() + i);
                }
            }
        }

        if (coins.hasCollided(pacman.getXpos(), pacman.getYpos())) {
            pacman.setPoints(pacman.getPoints() + 10);
            for (int i = 0; i < vcoins.size(); i++) {
                if (vcoins[i].x == pacman.getXpos() &&
                    vcoins[i].y == pacman.getYpos()) {
                    vcoins.erase(vcoins.begin() + i);
                }
            }
        }

        

        if (pacman.getXpos() == 0 && pacman.getYpos() == 13 && pacman.getDirection() == LEFT) {
            pacman.setXpos(27);
            pacman.setYpos(13);
        } else if (pacman.getXpos() == 27 && pacman.getYpos() == 13 && pacman.getDirection() == RIGHT) {
            pacman.setXpos(0);
            pacman.setYpos(13);
        } else if (pacman.getDirection() == UP) {
            pacman.move(DOWN, pacman.getXpos(), pacman.getYpos(), map);
        } else if (pacman.getDirection() == DOWN) {
            pacman.move(UP, pacman.getXpos(), pacman.getYpos(), map);
        } else if (pacman.getDirection() == RIGHT) {
            pacman.move(RIGHT, pacman.getXpos(), pacman.getYpos(), map);
        } else if (pacman.getDirection() == LEFT) {
            pacman.move(LEFT, pacman.getXpos(), pacman.getYpos(), map);
        }

        blinky.move(blinky.getDirection(), blinky.getXpos(), blinky.getYpos(),map);
        clyde.move(clyde.getDirection(), clyde.getXpos(), clyde.getYpos(), map);
        inky.move(inky.getDirection(), inky.getXpos(), inky.getYpos(), map);
        pinky.move(pinky.getDirection(), pinky.getXpos(), pinky.getYpos(), map);

        if (coins.hasCollided(pacman.getXpos(), pacman.getYpos())) {
            pacman.setPoints(pacman.getPoints() + 10);
            for (int i = 0; i < vcoins.size(); i++) {
                if (vcoins[i].x == pacman.getXpos() &&
                    vcoins[i].y == pacman.getYpos()) {
                    vcoins.erase(vcoins.begin() + i);
                }
            }
        }

        if (fruits.hasCollided(pacman.getXpos(), pacman.getYpos())) {
            pacman.setPoints(pacman.getPoints() + 25);
            for (int i = 0; i < vfruits.size(); i++) {
                if (vfruits[i].x == pacman.getXpos() &&
                    vfruits[i].y == pacman.getYpos()) {
                    vfruits.erase(vfruits.begin() + i);
                }
            }
        }

        if (first.hasCollided(pacman.getXpos(), pacman.getYpos()) ||
            second.hasCollided(pacman.getXpos(), pacman.getYpos()) ||
            third.hasCollided(pacman.getXpos(), pacman.getYpos()) ||
            forth.hasCollided(pacman.getXpos(), pacman.getYpos())) {
            pacman.setPower(true);
            for (int i = 0; i < vpowerups.size(); i++) {
                if (vpowerups[i].x == pacman.getXpos() &&
                    vpowerups[i].y == pacman.getYpos()) {
                    vpowerups[i].x == 0;
                    vpowerups[i].y == 0;
                    vpowerups.erase(vpowerups.begin() + i);
                }
            }
        } 

        if (pacman.hasPower() && !inky.isScared() && !pinky.isScared() && !blinky.isScared() && !clyde.isScared() && pacman.getPowerCounter() == 0) {
            inky.setScared(true);
            pinky.setScared(true);
            blinky.setScared(true);
            clyde.setScared(true);
        } 
        else if (pacman.getPowerCounter() == 100) {
            pacman.setPower(false);
            pacman.resetPowerCounter();
            inky.setScared(false);
            pinky.setScared(false);
            blinky.setScared(false);
            clyde.setScared(false);
        }
        else if (pacman.hasPower()) {
            pacman.setPowerCounter();
        }

        if (blinky.hasCollided(pacman.getXpos(), pacman.getYpos(), blinky.getXpos(), blinky.getYpos())) {

            if (pacman.hasPower() && blinky.isScared()) {
                blinky.setScared(false);
                blinky.setObject(3);
                pacman.setPoints(pacman.getPoints() + 50);
            } 
            else {
                pacman.setLifes(pacman.getLifes() - 1);
                pacman.setObject();
                inky.setObject(1);
                pinky.setObject(2);
                blinky.setObject(3);
                clyde.setObject(4);
            }
        }
        if (clyde.hasCollided(pacman.getXpos(), pacman.getYpos(), clyde.getXpos(), clyde.getYpos())) {
            if (pacman.hasPower() && clyde.isScared()) {
                clyde.setScared(false);
                clyde.setObject(4);
                pacman.setPoints(pacman.getPoints() + 50);
            } else {
                pacman.setLifes(pacman.getLifes() - 1);
                pacman.setObject();
                inky.setObject(1);
                pinky.setObject(2);
                blinky.setObject(3);
                clyde.setObject(4);
            }
        }
        if (inky.hasCollided(pacman.getXpos(), pacman.getYpos(), inky.getXpos(), inky.getYpos())) {
            if (pacman.hasPower() && inky.isScared()) {
                inky.setScared(false);
                inky.setObject(1);
                pacman.setPoints(pacman.getPoints() + 50);
            } else {
                pacman.setLifes(pacman.getLifes() - 1);
                pacman.setObject();
                inky.setObject(1);
                pinky.setObject(2);
                blinky.setObject(3);
                clyde.setObject(4);
            }
        }
        if (pinky.hasCollided(pacman.getXpos(), pacman.getYpos(), pinky.getXpos(), pinky.getYpos())) {
            if (pacman.hasPower() && pinky.isScared()) {
                pinky.setScared(false);
                pinky.setObject(2);
                pacman.setPoints(pacman.getPoints() + 50);
            } else {
                pacman.setLifes(pacman.getLifes() - 1);
                pacman.setObject();
                inky.setObject(1);
                pinky.setObject(2);
                blinky.setObject(3);
                clyde.setObject(4);
            }
        }
        // Set the score
        ui.setScore(pacman.getPoints()); // <-- Pass correct value to the setter

        // Set the amount of lives
        ui.setLives(pacman.getLifes()); // <-- Pass correct value to the setter

        // Render the scene
        std::vector<GameObjectStruct> objects = {
            pacman.getObject(), inky.getObject(), pinky.getObject(),
            blinky.getObject(), clyde.getObject()};
        objects.insert(objects.begin(), vfruits.begin(), vfruits.end());
        objects.insert(objects.begin(), vpowerups.begin(), vpowerups.end());
        objects.insert(objects.begin(), vcoins.begin(), vcoins.end());


        // ^-- Your code should provide this vector somehow (e.g.
        // game->getStructs())
        ui.update(objects);

        while (!SDL_TICKS_PASSED(SDL_GetTicks(), timeout)) {
            // ... do work until timeout has elapsed
        }
    }

    SDL_RemoveTimer(timer_id);

    return 0;
}