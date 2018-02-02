import sys

def get_mult_str(mult_str, multiplier):
    
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
    
def break_array_by_load_type(panel_array):
    pos_set, neg_set = [], []
    
    for panel in panel_array:
        if panel>0:
            pos_set += [panel]
        elif panel<0:
            neg_set += [panel]
            
    return pos_set, neg_set

def answer(xs):
    
    # if only one panel return immediately
    if len(xs) == 1:
        return str(xs[0])
        
    pos_set, neg_set = break_array_by_load_type(xs)
    
    #discard smallest -ve from odd # -ves
    neg_set = sorted(neg_set)
    if len(neg_set)%2 == 1:
        neg_set = neg_set[:-1]
        
    neg_set = list(map(lambda x:-x, neg_set))
    total_abs_power_set = pos_set+neg_set
    
    prod = '1' if len(total_abs_power_set)>0 else '0'
    for power in total_abs_power_set:
        prod = get_mult_str(prod, power)
        
    return prod
    
    
if __name__ == '__main__':
    print(answer(list(map(int, sys.argv[1:]))))
