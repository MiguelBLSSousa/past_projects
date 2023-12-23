#include "GameObjectStruct.h"
#include "UI.h"

#include <SDL.h>

#include <map>
#include <vector>

class Powerup
{
  public:
    int getXpos();
    void setXpos(int new_x_pos);
    int getYpos();
    void setYpos(int new_y_pos);
    bool hasCollided(int x, int y);
    void setObject(int x, int y);
    GameObjectStruct getObject();

  private:
    int x_pos;
    int y_pos;
    GameObjectStruct energiser;
};