#include <iostream>
using namespace std;

#define IN_QUEUE 0
#define AT_COUNTER 1
#define OUT_QUEUE 2
// Either before entering or after exiting.
#define INVALID -1

class Person {
  public:
	//Person();
	Person(int require);
	~Person() {};
	void PrintInfo();

	int person_id_;
	int  counter_;
	pair<int, int> status_till_;
	int goods_required_;
	int goods_collected_;	
	int in_queue_entry_time_;
	int counter_entry_time_;
	int out_queue_entry_time_;
	int out_queue_exit_time_;
};
/*	
Person::Person() {
	counter_ = -1;
	status_till_.first = INVALID;
	status_till_.second = -1;
	goods_collected_ = 0;
};
*/
Person::Person(int require) {
	counter_ = -1;
	status_till_.first = IN_QUEUE;
	goods_collected_ = 0;
	goods_required_ = require;
	in_queue_entry_time_ = -1;
	counter_entry_time_ = -1;
	out_queue_entry_time_ = -1;
	out_queue_exit_time_ = -1;
}

void Person::PrintInfo() {
	cout << "Person Id: " << person_id_ << endl;
	cout << "Status: " << status_till_.first << " till: "
		 << status_till_.second << endl;
	cout << "Counter: " << counter_ << endl;
	cout << "Goods collected: " << goods_collected_ << endl;
	cout << "Goods required: " << goods_required_ << endl;
	cout << "In queue entry time: " << in_queue_entry_time_ << endl;
	cout << "Counter entry time: " << counter_entry_time_ << endl;
	cout << "Out queue entry time: " << out_queue_entry_time_ << endl;
	cout << "Out queue exit tme: " << out_queue_exit_time_ << endl;
}
