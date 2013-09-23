#include <iostream>

#include "counter.cpp"

using namespace std;

#define VIEW_PERSON 1
#define VIEW_COUNTER 2 
#define VIEW_IN_QUEUE 3
#define VIEW_OUT_QUEUE 4

extern int time_;

class Display {
  public:
	Display(int view, vector<Person*>* persons, QueueSimulator* in_queue,
			QueueSimulator* out_queue, vector<Counter*>* counters);
	~Display() {};

	int view_;
	vector<Person*>* persons_;
   	QueueSimulator* in_queue_;
	QueueSimulator* out_queue_;
	vector<Counter*>* counters_;
	void Print();
	void PrintFinal();

  private:
	void PrintPersonView();
	void PrintCounterView();
	void PrintInQueueView();
	void PrintOutQueueView();
};

Display::Display(int view, vector<Person*>* persons, QueueSimulator* in_queue,
				QueueSimulator* out_queue, vector<Counter*>* counters) {
	view_ = view;
	persons_ = persons;
   	in_queue_ = in_queue;
	out_queue_ = out_queue;
	counters_ = counters;
}

void Display::Print() {
	switch (view_) {
		case VIEW_PERSON:
			PrintPersonView();
			break;
		case VIEW_COUNTER:
			PrintCounterView();
			break;
		case VIEW_IN_QUEUE:
			PrintInQueueView();
			break;
		case VIEW_OUT_QUEUE:
			PrintOutQueueView();
			break;
	}
}

void Display::PrintPersonView() {
	cout << "Time " << time_ << endl << endl;
	for (int i = 0; i < persons_->size(); ++i) {
		(*persons_)[i]->PrintInfo();
		//cout << endl;
	}
	cout << "_________________________________" << endl;
}

void Display::PrintInQueueView() {
	cout << "Time " << time_ << endl << endl;
	cout << "In Queue ";
	in_queue_->PrintInfo();
	cout << "_________________________________" << endl;
}

void Display::PrintOutQueueView() {
	cout << "Time " << time_ << endl << endl;
	cout << "Out Queue ";
	out_queue_->PrintInfo();
	cout << "_________________________________" << endl;
}

void Display::PrintCounterView() {
	cout << "Time " << time_ << endl << endl;
	for (int i = 0; i < counters_->size(); ++i) {
		(*counters_)[i]->PrintInfo();
		cout << endl;
	}
	cout << "_________________________________" << endl;
}

void Display::PrintFinal() {
	cout << "Final Snapshot" << endl;
	PrintInQueueView();
	PrintOutQueueView();	
}	
