import sys
from collections import deque

def get_valid_next_cords(maze, cord, prev_cord, err, h, w):
    next_valid_cords = []
    
    def cord_value(cord):
        return maze[cord[0]][cord[1]]

    def add_valid_top():
        new_err = err
        top = (cord[0]-1, cord[1])
        
        if cord[0]==0 or prev_cord == top:
            return

        if cord_value(top) == 1:

            if err == 0:
                return
            else:
                new_err -= 1

        next_valid_cords.append((top, new_err))

    def add_valid_bottom():
        new_err = err
        bottom = (cord[0]+1, cord[1])

        if cord[0]==h-1 or prev_cord == bottom:
            return

        if cord_value(bottom) == 1:

            if err == 0:
                return
            else:
                new_err -= 1
    
        next_valid_cords.append((bottom, new_err))

    def add_valid_left():
        new_err = err
        left = (cord[0], cord[1]-1)
        
        if cord[1]==0 or prev_cord == left:
            return

        if cord_value(left) == 1:

            if err == 0:
                return
            else:
                new_err -= 1

        next_valid_cords.append((left, new_err))
        
    def add_valid_right():
        new_err = err
        right = (cord[0], cord[1]+1)
        
        if cord[1]==w-1 or prev_cord == right:
            return

        if cord_value(right) == 1:

            if err == 0:
                return
            else:
                new_err -= 1
            
        next_valid_cords.append((right, new_err))
        
    add_valid_top()
    add_valid_bottom()
    add_valid_left()
    add_valid_right()
    
    return next_valid_cords


def maze_get_next(maze, curr_cord, prev_cord, err, h, w):
    cord_stack = deque([(curr_cord, err, prev_cord, 0)])
    move = 0
    visited_cords = [(0,0),0]
    #min_moves = 10000 # to serve as inf
    
    while(len(cord_stack)>0):
        curr_cord, err, prev_cord, move = cord_stack.popleft()
        #print (curr_cord, move)
                        
        if curr_cord == (h-1,w-1):
            return move+1

        next_cords = get_valid_next_cords(maze, curr_cord, prev_cord, err, h, w)
        
        for x in next_cords:
            if x not in visited_cords:
                cord_stack.append(x+(curr_cord, move+1))
                visited_cords.append(x)            
        
def answer(maze):
    
    err = 1
    start = (0,0)

    w = len(maze[0])
    h = len(maze)
    
    return maze_get_next(maze, start, start, err, h, w)


if __name__ == '__main__':
    print (answer([[0,0],[1,0]]))
    print (answer([[0,1,1,0],[0,0,0,1],[1,1,0,0],[1,1,1,0]]))
    print (answer([[0, 0, 0, 0, 0, 0], [1, 1, 1, 1, 1, 0], [0, 0, 0, 0, 0, 0], [0, 1, 1, 1, 1, 1], [0, 1, 1, 1, 1, 1], [0, 0, 0, 0, 0, 0]]))
    print (answer([[0]*20]*20))
