# This problem seems more mathematical in nature than computational.
# We have n bunnies, atleast m of whom are required to open the locks.
# Let us assume the total keys are x and each bunny gets a unique subset
# of x. No bunny receives more than one copy for a key.

# Key Observations:

# The minimum number of repeats in keys should be > the number of bunnies
# that can be left out while we still can open cells, so that we never come
# across a case where we left out all the bunnies that had a certain key while
# picking m bunnies out of n (Pigeon hole principle). So required copies
# for each key will be n-m+1 ('repeats').

# We will try to distribute a key and all of its 'repeat' copies uniquely
# to the bunnies such that each subset of bunny that receives a type of key
# will be unique. Notice how each such selection is essentially the same as
# a selection of m-1 bunnies that will not have that type of key
# (n-repeat = n-(n-m+1) = m-1). Assignng unique key copies like this for all
# possible subsets that have 'repeat' number of bunnies, we also cover all
# possible subsets of m-1 bunnies from n, all of which are missing
# one unique key. This way of assignment ensures that all groups of m-1
# or fewer bunnies wil always have a key missing among them so no group
# of m-1 bunnines will be able to release the prisoners.  

# Adding the key copies, lowest to highest key index and also along
# lowest to highest bunny index allows to get lexicographic least such
# key distributuin (i.e for 4 total bunnies and 3 repeats
# bunnies 0,1,2 get key 0, then bunnies 0,1,3 get key 1, then
# bunnies 0,2,3 get key 2, and finally bunnies 1,2,3 get key 3)
# The code to do this now becomes relatively straightforward and quick.

import sys
from itertools import combinations
     

def answer(num_buns, num_required):
    repeat = num_buns+1-num_required  # copies of each key required

    # assign bins for each bunny, that will hold all their keys
    bins = [[] for i in range(num_buns)]

    key = 0

    # Iteate through all possible combinations of
    # 'repeat' # of bunnies from all bunnies, lowest
    # to highest, and add key indexes also lowest to highest
    for tup in combinations(range(num_buns), repeat):
        for i in tup:
            bins[i].append(key)

        key += 1
        
    return bins


if __name__ == '__main__':
    print(answer(int(sys.argv[1]),int(sys.argv[2])))

    



