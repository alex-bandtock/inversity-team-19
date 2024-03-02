import random
import time
import json
import requests
from datetime import datetime, timedelta

cell_state = ['balanced', 'unbalanced']
SNs = [f'BT{i:03d}' for i in range(1, 51)]  # Generating 50 serial numbers

# Dictionary to track SOC occurrences for each SN
soc_counter = {sn: 0 for sn in SNs}

# Dictionary to track cycle count for each SN
charge_cycles = {sn: random.randrange(1, 1000) for sn in SNs}

# Dictionary to track starting SOC for each SN
starting_soc = {sn: random.randrange(0, 99) for sn in SNs}

# Dictionary to track random initial date for each SN
random_dates = {sn: datetime.now() - timedelta(days=random.randint(1, 365*5)) for sn in SNs}

# Endpoint URL
url = "http://localhost:8080/ingest/v1/api"

while True:
    for sn in SNs:
        soc = starting_soc[sn]  # Use the same starting SOC for each loop
        output_voltage = random.uniform(380, 500)  # Simulating voltage fluctuations during use and charging
        charge_rate = max(0, random.uniform(-1, 1))  # Ensure chargeRate is non-negative
        temperature = random.uniform(20, 50)  # Simulating temperature variations
        
        # Calculate new SOC based on output voltage and charge rate
        soc -= charge_rate * 0.1  # Adjust SOC based on charge/discharge rate
        if output_voltage > 420:  # Charging
            soc += 0.2
        elif output_voltage < 400:  # Discharging
            soc -= 0.3
        soc = max(0, min(99, soc))  # Ensure SOC remains within valid range
        
        # Randomly choose cell balance
        cell_balance = random.choice(cell_state)
        
        # Increment SOC occurrence counter
        soc_counter[sn] += 1
        
        # Check if SOC has appeared 10 times, then decrement SOC
        if soc_counter[sn] >= 10:
            soc -= 1
            soc_counter[sn] = 0  # Reset SOC occurrence counter
        
        # Calculate battery age in days
        battery_age_days = (datetime.now() - random_dates[sn]).days
        
        # Construct JSON object
        battery_data = {
            "time": datetime.now().isoformat(),
            "serialNumber": sn,
            "payload": {
                "soc": round(soc, 2),
                "outputVoltage": round(output_voltage, 2),
                "chargeRate": round(charge_rate, 2),
                "temperature": round(temperature, 2),
                "chargeCycles": charge_cycles[sn],
                "cellBalance": cell_balance,
                "batteryAge": battery_age_days
            }
        }
        
        # Convert battery data to JSON
        payload = json.dumps(battery_data)
        
        # Send POST request to the endpoint
        response = requests.post(url, data=payload)
        
        # Check if the request was successful
        if response.status_code == 200:
            print("Data posted successfully!")
        else:
            print(f"Failed to post data. Status code: {response.status_code}")
        
        # Simulate time passing
        time.sleep(60)
