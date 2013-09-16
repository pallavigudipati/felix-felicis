#include <iostream>

#include "simulator.h"

using namespace std;

void Simulator::LoadSpecifications() {
	// Counters.
	int num_counters;
	cin >> num_counters;
	int initial_goods;
	for (int i = 0; i < num_counters; ++i) {
		cin >> initial_goods;
		if (initial_goods < 0) {
			cout << "ERROR: Please input a non-negative value."
				 << endl;
			i--;
			continue;
		}
		Counter* counter = new Counter(initial_goods);
		counter->counter_id = i + 1;
		counters_.push_back(counter);
	}

	// In-Queue
	int rate;
	cin >> rate;
	rate_in_queue_ = rate;
	int initial_size;
	cin >> initial_size;
	in_queue_.size_ = initial_size;
	last_person_id = initial_size;

	// Out-Queue

	// View
	int view;
	cin >> view;
	view_ = view;
}

void Simulator::RunSimulation() {
	int time = 0;
	while(1) {
		
		time++;			
	}
}
