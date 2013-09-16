#include <iostream>
using namespace std;

#define IN_QUEUE 0
#define AT_COUNTER 1
#define OUT_QUEUE 2
// Either before entering or after exiting.
#define INVALID -1

class Person {
  public:
	Person() {
		status = INVALID;
		goods_collected = 0;
	};
	~Person() {};
	void PrintInfo();

	int person_id;
	pair<int, int> status_till_;
	int goods_required_;
	int goods_collected_;	
	int in_queue_entry_time_;
	int counter_entry_time_;
	int out_queue_entry_time_;
	int out_queue_exit_time_;
};
	
Person::Person() {
	status_till_.first = INVALID;
	status_till_.second = -1;
	goods_collected_ = 0;
};

Person::Person(int require) {
	status_till_.first = INVALID;
	goods_collected_ = 0;
	goods_requirement_ = require;
}
// Check Properly - neeche waala kuch bhi likh diya neend mein :/
void Person::PrintInfo() {
	cout << "Person ID: " << person_id << endl;
	cout << "  Status: " << status_till_.first << endl;
}
