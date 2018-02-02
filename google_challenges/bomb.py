# If we have M,F as x,y for cycle n, then the M,F for cycle n+1 will
# be either x+y,y or x,x+y. Either way, the larger of the two will be
# sum of the previous M and F. Looking at this backwards, we can compute
# the value of M,F for cycle n-1, by keeping the smaller of M,F at cycle
# n the same, and the other as the differene of the two. Repeating
# this process backward, we will either hit M,F as 1,1 (The cycle count
# when it does will be our required answer) or get some other pair with
# equal values and demontrate the combination is unattainable.

# An observaton in this process is that we will keep on subtracting
# the smaller bomb count from the larger as we go back each cycle until
# it becomes the smaller of the two. To speed up this process that could
# otherwise take a very long, we can divide the larger of the two (say Bomb S)
# with the smaller since the remainder from division will be the new value
# of the Bomb S when it becomes the lower one and the quotient will be the
# number of backward steps it took to reach that point. Bomb S now the
# divisor for second iteration. Successively repeating the division process
# will considerably speed up the time to find the solution.

# This approach bears a stiking resemblance to the advanced euclid process
# of calculating GCD of two numbers. 

import sys

# Return if int for num_str > int for ref_str
def is_greater(num_str, ref_str):
    num_str = str(int(num_str))
    ref_str = str(int(ref_str))
    
    if len(num_str) > len(ref_str):
        return True
    
    if len(num_str) == len(ref_str):
        for i, j in zip(num_str, ref_str):
            if int(i)<int(j):
                return False
            elif int(i)>int(j):
                return True
    
    return False

# Subtracts y from x (both str representation of ints)
# and returns diff as a str. Asserts x is always >= y 
def subtract(x,y):
    assert(len(x)>=len(y))

    y_stuffer = '0'*(len(x)-len(y))
    y = y_stuffer + y
    
    diff = ''
    carry = 0
    for i, j in zip(x[::-1], y[::-1]):
        _x = int(i)
        _y = int(j)
        
        if _x == 0  and carry != 0:
            _x = 9
            carry = -1
        else:
            _x += carry 
            carry  = 0
            
        if _x<_y:
            _x = _x+10
            carry = -1
        
        diff = str(_x -_y) + diff
        
    return str(int(diff)) #removes leading 0s


# Adds x and y (both str representation of ints)
# and returns the sum as a str.
def addition(x,y):

    stuffer = '0'*abs(len(x)-len(y))
    
    if len(x)>len(y):
        y = stuffer + y
    else:
        x = stuffer + x
    
    add_str = ''
    carry = 0
    for i, j in zip(x[::-1], y[::-1]):
        _x = int(i)
        _y = int(j)
        
        add = _x + _y + carry
        carry = int(add/10)
        
        add_str = str(add%10) + add_str
        
    if carry>0:
        add_str = str(carry) + add_str
                
    return str(int(add_str)) #removes leading 0s


# Multiplies x and y (both str representation of ints)
# and returns the product as a str.
def multiply(mult_str, multiplier):
    
    carry = 0
    prod_str = ''
    for digit in mult_str[::-1]:
        digit = int(digit)
        
        prod = digit*multiplier + carry
        carry = int(prod/10)
        
        prod_str = str(prod%10) + prod_str
        
    if carry>0:
        prod_str = str(carry) + prod_str
        
    return prod_str


# Divides y by x (both str representation of ints)
# and returns quotient and remainder as strings.
# Asserts x is always >= y and x,y > 0 
def divide(x,y):
    y_len = len(y)
    x_len = len(x)
    fact = '' 
    
    i = y_len
    x_ = x[:y_len]
    while True:
    
        if is_greater(y, x_):
            if i<=x_len:
                fact += '0'

            if i >= x_len:
                return str(int(fact)), str(int(x_))

            x_ += x[i]
            i += 1
            continue
        
        prev = y
        for f in range(2,11):
            mult = multiply(y, f)
        
            if is_greater(mult, x_):
                fact += str(f-1)
                x_ = subtract(x_, prev)

                if i < x_len:
                    x_ += x[i]
                break
            
            prev = mult
        
    
        i += 1

    #return str(int(fact)), str(int(x_))        


# Function utilizing advanced Euclid's theorm for GCD
def answer(M, F):
    cycle = ''
    while(True):
                
        if M == '1':
            return subtract(addition(cycle, F), "1")

        if F == '1':
            return subtract(addition(cycle, M), "1")
        
        if M == '0' or F == '0':
            break
        
        if is_greater(M,F):
            fact, M = divide(M,F)
        else:
            fact, F = divide(F,M)
        
        cycle = addition(cycle, fact)

    return "impossible"


if __name__ == '__main__':
    print(answer(sys.argv[1], sys.argv[2]))
    #print ("fact = "+fact+'\t'+"rem = "+x_)
