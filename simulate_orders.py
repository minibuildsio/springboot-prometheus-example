import threading
import time
import random
import requests

def create_order():
  return requests.post('http://localhost:8080/createOrder', json={"items": ["item1", "item2"]}).json()['id']


def complete_order(id):
  requests.post(f'http://localhost:8080/completeOrder/{id}')


def create_and_complete_order():
  time.sleep(random.randint(0, 10))

  order_id = create_order()
  
  time.sleep(max(0, random.gauss(40, 10)))

  complete_order(order_id)


threads = [threading.Thread(target=create_and_complete_order) for _ in range(10000)]
[t.start() for t in threads]
[t.join() for t in threads]
