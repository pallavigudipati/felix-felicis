#include <iostream>
#include "person.cpp"
using namespace std;

class QueueSimulator {
  public:
	QueueSimulator() {}
	QueueSimulator(vector<Person*> initial_crowd) {}
	~QueueSimulator() {}
	void JoinQueue(Person* person);
	Person* LeaveQueue();

	int size_;
	
  private:
	queue<Person*> queue_simulator_;
}

QueueSimulator::QueueSimulator() {
	size_ = 0;
}

QueueSimulator::QueueSimulator(vector<Person*> initial_crowd) {
	size_ = initial_crowd.size();
	for (int i = 0; i < initial_crowd.size(); ++i) {
		// check push/pop syntax
		queue_simulator_.push(initial_crowd[i]);
		initial_crowd[i]->person_id = i + 1;
	}
}

void QueueSimulator::JoinQueue(Person* person) {
	queue_simulator_.push(person);
}

Person* QueueSimulator::LeaveQueue() {
	return queue_simulator_.pop(); //???
}

