from time import sleep
import yaml

from google.cloud import pubsub_v1


def callback(message):
    print(f"New line: \n {message.data} \n")
    message.ack()


print("1")
with open("config.yaml", "r") as f:
    data = yaml.safe_load(f)

print("2")
subscriber = pubsub_v1.SubscriberClient()

print("3")
topic_name = f"projects/{data['project']}/topics/{data['topic']}"

print("4")
subscription_name = f"projects/{data['project']}/subscriptions/subscription-python"

print("Try to connect subscriber")
future = subscriber.subscribe(subscription_name, callback)
print("Subscriber successfully connected")
while True:
    try:
        print("Try to get")
        future.result()
    except KeyboardInterrupt:
        future.cancel()
    sleep(data["sleep"])
