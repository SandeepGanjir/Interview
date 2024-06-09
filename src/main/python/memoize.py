from functools import lru_cache
from time import time
import atexit

def timer_func(func):
    # This function shows the execution time of
    # the function object passed
    def wrap_func(*args, **kwargs):
        t1 = time()
        result = func(*args, **kwargs)
        t2 = time()
        print(f'Function {func.__name__!r} executed in {(t2-t1):.4f}s')
        return result
    return wrap_func

@lru_cache
def fib(n: int) -> int:
    if (n <= 1):
        return n
    else:
        return fib(n - 1) + fib(n - 2)


@timer_func
def fibSeries(n: int) -> None:
    for i in range(0, n):
        print(f'{i} : {fib(i)}')

@atexit.register
def closing():
    print("Exiting Program")

def main():
    fibSeries(40)

main()
"""
if __name__ = "__main__":
    main()
"""