#include <iostream>

#include "person.cpp"
using namespace std;

// counter status
#define CLOSED false
#define OPEN true

class Counter {
  public:
	Counter() {};	
	Counter(int initial_goods);
	~Counter() {};
	bool AcceptRequest(Person* goods_request);
	void PrintInfo();

	int counter_id;
	pair<bool, int> status_till_;
	int max_num_goods_;
	int num_goods_;
	int closing_time_;

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
	status_till_.second = 0;
	max_num_goods_ = initial_goods;
	num_goods_ = initial_goods;
}

bool Counter::AcceptRequest(Person* person) {
	person->status_till_.first = AT_COUNTER;
	
	if (person->goods_required_ < num_goods_) {
		num_goods_ -= person->goods_request_;
		person->goods_required_ = 0;
		person->goods_collected_ = person->goods_required_;
		status_till_.second += person->goods_collected_;
	} else {
		person->goods_collected_ = num_goods_;
		person->goods_required_ -= num_goods_;
		closing_time_ = status_till_.second + num_goods_;
		num_goods_ = 0;
		status_till_.first = CLOSED;
		status_till_.second = -1;
	}
	
	person->status_till_.second = person->goods_collected_;
}

void Counter::PrintInfo() {
	int counter_id;
	pair<bool, int> status_till_;
	int max_num_goods_;
	int num_goods_;
	int closing_time_;
	
}
