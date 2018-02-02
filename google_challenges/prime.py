
# Class generates a sequece of potential candidates 
# i.e. numbers that are not even or divisible by 3
# to reduce search space, eg. 5 7 11 13 17 19 23 25 ...
class candidate_supplier:
    # every third odd number starting with 5 is divisible by 3 
    # so we can set for increments by 2 and 4 alternatively
    # reducing the overhead for checking a prime 
    
    def __init__(self):
        self.cand = 1  # seed
        self.increment = 4
        
    def next_cand(self):
        self.cand += self.increment
        self.increment = 6 - self.increment
        return self.cand    
        
# Checks if a given candidate from supplier is prime
def check_prime(prime_cand):
    divisor_iter = candidate_supplier()
    
    divisor = divisor_iter.next_cand()
    
    # we check up to cand/5 since the supplier generates
    # numbers that are not multiples of 2 and 3
    while (divisor <= prime_cand/5):  
        if prime_cand%divisor == 0:
            return False
        
        divisor = divisor_iter.next_cand()
        
    return True
    
def answer(n):
    prime_str = '23' #head start
    
    prime_cand_iter = candidate_supplier()
    
    while len(prime_str) < n+1+5:
        prime_cand = prime_cand_iter.next_cand()     
        if check_prime(prime_cand):
            prime_str += str(prime_cand)
            
    return prime_str[n:n+5]

if __name__ == '__main__':
    print (answer(10000))
