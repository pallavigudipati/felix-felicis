#include <iostream>

#include "queue_sim.cpp"
using namespace std;

// counter status
#define CLOSED 0
#define OPEN 1
#define OCCUPIED 2

int time_;

class Counter {
  public:
	Counter() {};	
	Counter(int initial_goods);
	~Counter() {};
	bool AcceptRequest(Person* goods_request);
	void PrintInfo();

	int counter_id_;
	pair<int, int> status_till_;
	int max_num_goods_;
	int num_goods_;
	int closing_time_;
	int person_id_;

};

Counter::Counter(int initial_goods) {
	/* Check all this while taking input
	if (initial_goods < 0) {
		cout << "ERROR: Initial number of goods should be" +
				" non-negative." << endl;
		
		exit(-1);
	}
	*/
	if (!initial_goods) {
		status_till_.first = CLOSED;
		status_till_.second = -1;
		return;
	}
	status_till_.first = OPEN;
	status_till_.second = -1;
	max_num_goods_ = initial_goods;
	num_goods_ = initial_goods;
	person_id_ = -1;
}

bool Counter::AcceptRequest(Person* person) {
	person->status_till_.first = AT_COUNTER;
	person_id_ = person->person_id_;	
	status_till_.first = OCCUPIED;

	if (person->goods_required_ < num_goods_) {
		num_goods_ -= person->goods_required_;
		person->goods_collected_ = person->goods_required_;
		status_till_.second = time_ + person->goods_collected_;
		person->goods_required_ = 0;
	} else {
		person->goods_collected_ = num_goods_;
		person->goods_required_ -= num_goods_;
		closing_time_ = time_ + num_goods_;
		status_till_.second = time_ + num_goods_;
		num_goods_ = 0;
	}
	
	person->status_till_.second = time_ + person->goods_collected_;
}

void Counter::PrintInfo() {
	cout << "Counter Id: " << counter_id_ << endl;
	cout << "Status: ";
	//cout << status_till_.first;

	if (status_till_.first == OPEN) {
	   cout << "OPEN";
	} else if (status_till_.first == OCCUPIED) {
 		cout << "OCCUPIED";
	} else {
 		cout << "CLOSED";
	}
	
	cout << "  till: " << status_till_.second << endl;
	cout << "Max number of goods: " <<  max_num_goods_ << endl;
	cout << "Present number of goods: " <<  num_goods_  << endl;
	cout << "Closing time: " << closing_time_ << endl;
	cout << "Person Id: " << person_id_ << endl;
}
