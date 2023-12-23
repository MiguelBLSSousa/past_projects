#include "GameObjectStruct.h"
#include "UI.h"

#include <SDL.h>

#include <map>
#include <vector>
#include <string>

class Ghost
{

  public:
    int getXpos();
    void setXpos(int new_x_pos);
    int getYpos();
    void setYpos(int new_y_pos);
    void setDirection(Direction newDirection);
    Direction getDirection();
    void move(Direction direction, int x, int y, std::vector<std::vector<int>> &map);
    bool hasCollided(int x_p, int y_p, int x, int y);
    void setScared(bool new_scared);
    bool isScared();
    void setObject(int ghostType);
    GameObjectStruct getObject();

  private:
    int x_pos;
    int y_pos;
    Direction direction;
    GameObjectStruct ghost;
    bool scared = false;
    std::string name;
};