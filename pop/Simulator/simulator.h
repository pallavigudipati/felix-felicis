#include <iostream>

#include "display.cpp"

using namespace std;

extern int time_;

class Simulator {
  public:
	  void LoadSpecifications();
	  void RunSimulation();

  private:
	  // Rate of population of in-queue.
	  // For now it's an integer > 0.
	  Display* display_;
	  void JoinExitQueue();
	  void JoinEntryQueue();
	  void ExitExitQueue();
	  void ExitEntryQueue();
	  vector<int> FindEmptyCounters();

	  int num_closed_;
	  int initial_size_;
	  int view_;
	  int rate_in_queue_;
	  int last_person_id_;
	  vector<Counter*> counters_;
	  QueueSimulator in_queue_;
	  QueueSimulator out_queue_;
	  vector<Person*> at_counters_;
	  vector<Person*> display_person_;
	  virtual int Sampling() {
		  return 1;
	  }
};
