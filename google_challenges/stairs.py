import sys


# Returns the minimum attainable height of a staircase
# for a set of bricks
def get_min_height(num):
	count=0
	while(num>0):
		count += 1
		num -= count

	return count


# Main function that returns the total # of stairs that can
# be created for a set of bricks. Iterates through the 
# valid set of heights for the largest step (between n-1 to
# minimum attainable height) and recursively calculates the 
# stairs to the left for the subsequent smaller # bricks 
# with a height constraint induced by the largest step. A
# speed up in recursion is obtained by storing state already
# visited.
def count_staircase_types(num, ht, state_count):

    def get_next_state_count(num, h):
        
        # compute value and store state if not already saved
        if (num, h) not in state_count:
            next_state_count = count_staircase_types(num, h, state_count)
            state_count[(num, h)] = next_state_count

        return state_count[(num, h)]

    min_ht = get_min_height(num)
    count = 0

    for i in reversed(range(min_ht, ht)):
        
        if i>num-i:
            count += 1      # current stair is valid
            new_ht = num-i
        else:
            new_ht = i

        next_state_count = get_next_state_count(num-i, new_ht)
        count += next_state_count
        
    return count


# The function that will be called. Initializes a state 
# counter to capture already computed states. A state  
# stores the total # of stairs possible for a given set of
# bricks and max height restriction for the resulting stairs.
def answer(n):
    state_count = {}
    return count_staircase_types(n, n, state_count)
    
    
print (answer(int(sys.argv[1])))
#print get_min_height(int(sys.argv[1]))

# 119816822
# 487067745 
# 2927262
