#include <iostream>

#include "simulator.h"

using namespace std;

void Simulator::LoadSpecifications() {
	// Counters.
	time_ = -1;
	int num_counters;
	cin >> num_counters;
	if (num_counters < 0) {
		cout << "ERROR: Please input a non-negative value." << endl;
		i--;
		continue;
	}
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
		counter->counter_id_ = i;
		counters_.push_back(counter);
	}

	// In-Queue
	int rate;
	cin >> rate;

	while (1) {
		if (rate < 0) {
			cout << "ERROR: Please input a non-negative value."
				 << endl;
		} 
		continue;
	}

	rate_in_queue_ = rate;
	cin >> initial_size_;
	last_person_id_ = 0;
	for (int i = 0; i < initial_size_; ++i) {
		int require;
		cin >> require;
		if (require < 0) {
			cout << "ERROR: Please input a non-negative value."
				 << endl;
			i--;
			continue;
		}
		Person* person = new Person(require);
		person->person_id_ = last_person_id_;
		last_person_id_++;
		person->in_queue_entry_time_ = time_;
		in_queue_.JoinQueue(person);
	} 

	// View
	int view;
	cin >> view;
	view_ = view;

	// Display
	display_ = new Display(view, &display_person_, &in_queue_, &out_queue_,
							&counters_); 
}

// TODO : print errors for rate and view not being integers
// time : after the end of t th second.
void Simulator::RunSimulation() {
	display_->Print();
	while(1) {
		time_++;	
		//cout << "a";
		//TODO : what if a person with zero goods requirement comes 
		JoinExitQueue();
		ExitExitQueue();
		JoinEntryQueue();
		ExitEntryQueue();
		display_->Print();
		if (num_closed_ == counters_.size()) {
			display_->PrintFinal();
			cout << "Simulation ended at time = " << time_ << endl;
			return;
		}
	}
}

void Simulator::ExitEntryQueue() {
	vector<int> open_counters = FindEmptyCounters();
	for (int i = 0; i < open_counters.size(); ++i) {
		if (!in_queue_.size_) {
			break;
		}
		Person* person = in_queue_.LeaveQueue();
		person->counter_entry_time_ = time_;
		at_counters_.push_back(person);
		person->counter_ = open_counters[i];
		//counters_[open_counters[i]]->status_till_.first = OCCUPIED;
		//cout << "#" << OCCUPIED;
		//cout << "!!" << counters_[open_counters[i]]->status_till_.first << endl;
		counters_[open_counters[i]]->AcceptRequest(person);
		//cout << counters_[open_counters[i]]->status_till_.first << endl;
	}
}

void Simulator::ExitExitQueue() {
	display_person_.clear();
	while (out_queue_.size_) {
		Person* person = out_queue_.queue_simulator_.front();
		if (person->status_till_.second == -1) {
			person->status_till_.second = time_ + person->goods_collected_;
		}
		if (person->status_till_.second <= time_) {
			out_queue_.LeaveQueue();
			person->out_queue_exit_time_ = time_;
			display_person_.push_back(person);
		} else {
			break;
		}
	}
}

void Simulator::JoinEntryQueue() {
	for (int i = 0; i < rate_in_queue_; ++i) {
		Person* person = new Person(Sampling());
		person->person_id_ = last_person_id_;
		last_person_id_++;
		person->in_queue_entry_time_ = time_;
		in_queue_.JoinQueue(person);
	}
}

void Simulator::JoinExitQueue() {
	for (int i = 0; i < at_counters_.size(); ++i) {
		Person* person = at_counters_[i];
		if (person->status_till_.second <= time_) {
			person->status_till_.first = OUT_QUEUE;
			person->status_till_.second = -1;
			person->out_queue_entry_time_ = time_;
			at_counters_.erase(at_counters_.begin() + i);
			out_queue_.JoinQueue(person);
		}
	}	
}

vector<int> Simulator::FindEmptyCounters() {
	vector<int> open_counters;
	num_closed_ = 0;
	for (int i = 0; i < counters_.size(); ++i) {
		if (counters_[i]->status_till_.first == CLOSED) {
			num_closed_++;
		} else if (counters_[i]->status_till_.first == OPEN) {
			if (!counters_[i]->num_goods_) {
				counters_[i]->status_till_.first = CLOSED;
				counters_[i]->status_till_.second = -1;
				num_closed_++;
				counters_[i]->person_id_ = -1;
			} else {
				open_counters.push_back(i);
			}
		} else {
			if (counters_[i]->status_till_.second <= time_) {
				if (!counters_[i]->num_goods_) {
					counters_[i]->status_till_.first = CLOSED;
					counters_[i]->person_id_ = -1;
					num_closed_++;
					counters_[i]->status_till_.second = -1;
				} else {
					counters_[i]->status_till_.first = OPEN;
					counters_[i]->person_id_ = -1;
					open_counters.push_back(i);
				}
			}
		}
	}
	return open_counters;
}

