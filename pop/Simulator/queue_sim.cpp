#include <iostream>
#include <list>
#include <vector>

#include "person.cpp"
using namespace std;

class QueueSimulator {
  public:
	QueueSimulator();
	~QueueSimulator() {}
	//void FillInitialCrowd(vector<Person*> initial_crowd);
	void JoinQueue(Person* person);
	Person* LeaveQueue();
	void PrintInfo();

	int counter_id_;
	int size_;
	list<Person*> queue_simulator_;
	
  private:
};

QueueSimulator::QueueSimulator() {
	size_ = 0;
}

/*
void QueueSimulator::FillInitialCrowd(vector<Person*> initial_crowd) {
	size_ = initial_crowd.size();
	for (int i = 0; i < initial_crowd.size(); ++i) {
		// check push/pop syntax
		queue_simulator_.push_back(initial_crowd[i]);
		initial_crowd[i]->person_id = i + 1;
	}
}
*/

void QueueSimulator::JoinQueue(Person* person) {
	queue_simulator_.push_back(person);
	size_++;
}

Person* QueueSimulator::LeaveQueue() {
	size_--;
	Person* person = queue_simulator_.front();
	queue_simulator_.pop_front();
	return person;
}

void QueueSimulator::PrintInfo() {
	for (list<Person*>::iterator it = queue_simulator_.begin();
		 it != queue_simulator_.end(); ++it)  {
		(*it)->PrintInfo();
		cout << endl;
	}
};
