#include "GameObjectStruct.h"
#include "UI.h"

#include <SDL.h>

#include <map>
#include <string>
#include <vector>

class Pacman
{
  public:

    int getXpos();
    void setXpos(int new_x_pos);
    int getYpos();
    void setYpos(int new_y_pos);
    void setDirection(Direction newDirection);
    Direction getDirection();
    void move(Direction direction, int x, int y, std::vector<std::vector<int>> &map);
    int getLifes();
    void setLifes(int newLifes);
    int getPoints();
    void setPoints(int points);
    void setPower(bool power);
    bool hasPower();
    void respawn();
    bool hasDied();
    void setObject();
    void setPowerCounter();
    void resetPowerCounter();
    int getPowerCounter();
    GameObjectStruct getObject();

  private:
    int x_pos;
    int y_pos;
    int points = 0;
    Direction direction;
    bool isPowered = false;
    bool isDead;
    int lifes = 3;
    int powercounter = 0;
    GameObjectStruct pacman;
};