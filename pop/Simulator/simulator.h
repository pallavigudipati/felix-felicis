#include <iostream>

#include "person.cpp"
#include "counter.cpp"
#include "queue_sim.cpp"

using namespace std;

#define VIEW_COUNTER 1
#define VIEW_PERSON 2
#define VIEW_IN_QUEUE 3
#define VIEW_OUT_QUEUE 4

class Simulator {
  public:
	  void LoadSpecifications();
	  void RunSimulation();
	  void PrintSnapshot();

  private:
	  // Rate of population of in-queue.
	  // For now it's an integer > 0.
	  int view_;
	  int rate_in_queue_;
	  int last_person_id;
	  vector<Counter*> counters_;
	  QueueSimulator in_queue_;
	  QueueSimulator out_queue_;
};
