# We perform an exhaustive search on a tree of all moves
# to find the best case of bunnies rescued. We use an 
# iterative process to move through the graph in a DFS 
# manner. To prune branches and speed up the search,
# we save states that we have exhusted completely.

# A state is defined as:
# (current_node, new_bunnies_picked, bunnies_rescued,
#  current_time_limit, descend_down)

# Current_node:       The node we are currently at
# Bunnies_rescued:    Bunnies we have rescued so far
# New_bunnies_picked: The new bunies picked after the
#                     last set was rescued
# Current_time_limit: Bulkhead closing time from that state
# Descend_down:       Flag that indicates if we can descend 
#                     down that state to the next valid ones

import sys
from itertools import combinations, permutations

# returns a hashable object for a state so that it 
# can be stored and searched for
def hashable(state):
    curr, new_bunnies_picked, bunnies_rescued, _ , _ = state
    bunnies_helped_str = ''.join(map(str,sorted(new_bunnies_picked+
                                                bunnies_rescued)))
    
    return (curr, bunnies_helped_str)


# the main function that executes the exhaustive search
def exhaustive_search(times, time_limit, max_add_back):

    # helper function to identify next valid moves 
    # that can be made from a state
    def get_valid_moves():
        valid_moves = []
        for node,time in enumerate(start):
            if node == curr:
                continue
            
            # to ensure complete exhaustion, we add the
            # maximum time that can be added back to each move 
            if time <= curr_time_limit+max_add_back:
                valid_moves.append((node,time))

        return valid_moves

    # initialize variables
    states_exhausted = {}
    travelled_states = {}  
    best_case = []
    state_stack = [(0, [], [], time_limit, True)]

    # go through a state stack to implement DFS like process to get
    # the final solution
    while state_stack!=[]:
        curr_state = state_stack.pop()
        (curr, new_bunnies_picked, bunnies_rescued,
         curr_time_limit, descend) = curr_state 

        if curr not in new_bunnies_picked+bunnies_rescued+[0,len(times)-1]:
            new_bunnies_picked.append(curr)

        curr_hash = hashable(curr_state)
        
        # check if all possible moves for this state are exhausted.
        # If exhausted, then save state in exhausted set so that 
        # we can prune the move tree if we encounter this state
        # with the same or worse time limit.
        if descend == False:
            states_exhausted[curr_hash] = curr_time_limit
            del travelled_states[curr_hash]
            continue

        # don't proceed if this state was encountered earlier 
        # within the same path, with same or better time limit
        last_time = travelled_states.get(curr_hash)
        if last_time != None and last_time >= curr_time_limit:
            continue
        
        # if we are at the bulkhead, then we can at least save 
        # all the bunnies we have helped so far in this path. 
        # If that is better than the existing best case, then
        # this becomes the new best case
        if curr == len(times)-1 and curr_time_limit>=0:
            bunnies_rescued += new_bunnies_picked
            bunnies_rescued.sort()

            if len(bunnies_rescued)>=len(best_case):
                best_case = bunnies_rescued
                
            new_bunnies_picked = []

        # if we have rescued all the bunnies, we can
        # just exit and return an all bunnies set 
        if len(bunnies_rescued)== len(times)-2:
            return bunnies_rescued
        
        # if the current state was exhauste earlier with the
        # same or worse time limit, we don't have to go further 
        # as we have already done that once and registered the
        # best outcome
        rec_time = states_exhausted.get(curr_hash)        
        if rec_time and curr_time_limit <= rec_time:
            # If this state never reached bulkhead, then we can 
            # check to see if the current bunnies rescued are better
            if len(bunnies_rescued)>=len(best_case):
                best_case = bunnies_rescued

            continue

        # get next valid moves
        start = times[curr]
        valid_moves = get_valid_moves()

        # change the descend flag to false
        curr_state = (curr, new_bunnies_picked, bunnies_rescued,
                      curr_time_limit, False)

        # register state in travelled state for next moves and
        # stack placeholder to detect current state exhaustion
        travelled_states[curr_hash] = curr_time_limit
        state_stack.append(curr_state)    
        
        # make all the next valid moves and add the new states
        # to the state stack
        for node, time in valid_moves:
            new_time_limit =  curr_time_limit-time                
            next_state = (node, list(new_bunnies_picked),
                          list(bunnies_rescued), new_time_limit, True)

            state_stack.append(next_state)
    
    # return the best case for rescue after we have 
    # exhauseted all possibilities 
    return best_case


# Use bellman-ford to check for -ve weight cycles.
def get_max_return_cycle(graph):
    vertices = len(graph)
    min_dist = [0]+[float('inf')]*(vertices-1)
    prev = [None]*vertices

    for i in range(1,vertices):
        for i,j in permutations(range(vertices),2):
            if min_dist[i] + graph[i][j] < min_dist[j]:
                min_dist[j] = min_dist[i] + graph[i][j]
                prev[j] = i
    
    for i,j in permutations(range(vertices),2):
        if min_dist[i] + graph[i][j] < min_dist[j]:
            return True

    return False


# Returns maximum time that can be added back in the graph,
# useful to determine halt point
def max_add_time(times):
    ret = 0
    for lst in times:
        ret = min(lst+[ret])

    return -ret


# the function that is called with a graph and a time limit
def answer(times, time_limit):
    
    # check for -ve cycles. If it exists then all
    # bunnies can be saved
    negative_cycle = get_max_return_cycle(times)
    if negative_cycle:
        return list(range(len(times)-2))

    max_add_back = max_add_time(times)

    outcome = exhaustive_search(times, time_limit, max_add_back)
    
    # since bunny ids are one less than node id
    final_res = [x-1 for x in outcome]

    return final_res
