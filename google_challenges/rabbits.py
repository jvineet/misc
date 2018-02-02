
def rabbits(i,k):
    if i==1 or i==2:
        return 1

    return k*rabbits(i-2,k)+rabbits(i-1,k)  ##k offspring pairs instead of 1

print(rabbits(28,4))
