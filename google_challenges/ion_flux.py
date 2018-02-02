import sys

def get_perfect_post_order_parent(root, val, height):
    left = root - 2**(height-1)
    right = root - 1 
        
    if height == 1:
        return -1

    if val >= root:
        return -1

    if val in [left,right]:
        return root
    
    if val<left:
        return get_perfect_post_order_parent(left, val, height-1)
    else:
        return get_perfect_post_order_parent(right, val, height-1)

def answer(h, q):
    root = 2**h - 1
    lam = lambda x: get_perfect_post_order_parent(root,x,h)
    return list(map(lam, q))
    
if __name__ == '__main__':
    print(answer(int(sys.argv[1]), list(map(int, sys.argv[2:]))))
