# Project-2-Clinic-Simulation-with-Threads-and-Semaphores
This project will simulate a visit to the doctor’s office using threads and semaphores.

Overview

The clinic to be simulated has doctors, each of which has their own nurse.  Each doctor has an office of his or her own in which to visit patients.  Patients will enter the clinic to see a doctor, which should be randomly assigned.  Initially, a patient enters the waiting room and waits to register with the receptionist.  Once registered, the patient sits in the waiting room until the nurse calls.  The receptionist lets the nurse know a patient is waiting.  The nurse directs the patient to the doctor’s office and tells the doctor that a patient is waiting.  The doctor visits the patient and listens to the patient’s symptoms.  The doctor advises the patient on the action to take.  The patient then leaves.


Threads

Receptionist – one thread
Doctor – one thread each, maximum of 3 doctors
Nurse – one per doctor thread, identifier of doctor and corresponding nurse should match
Patient – one thread each, up to 15 patients


Inputs

The program should receive the number of doctors and patients as command-line inputs.  



Other rules:
1)	All mutual exclusion and coordination must be achieved with semaphores.  
2)	A thread may not use sleeping as a means of coordination.  
3)	Busy waiting (polling) is not allowed. 
4)	Mutual exclusion should be kept to a minimum to allow the most concurrency.
5)	Each thread should only print its own activities.  The patient threads prints patient actions and the doctor threads prints doctor actions, etc.
6)	Your output must include the same information and the same set of steps as the sample output.
 

Output

1)	Each step of each task of each thread should be printed to the screen with identifying numbers so it is clear which threads are involved.  
2)	Begin by printing the number of patients, nurses, and doctors in this run.
3)	Thread activity output sample.  The wording in your output should exactly match the sample:

Run with 3 patients, 3 nurses, 3 doctors

Patient 0 enters waiting room, waits for receptionist
Receptionist registers patient 0
Patient 0 leaves receptionist and sits in waiting room
Patient 2 enters waiting room, waits for receptionist
Nurse 0 takes patient 0 to doctor's office
Receptionist registers patient 2
Patient 0 enters doctor 0's office
Patient 2 leaves receptionist and sits in waiting room
Patient 1 enters waiting room, waits for receptionist
Nurse 2 takes patient 2 to doctor's office
Receptionist registers patient 1
Patient 2 enters doctor 2's office
Doctor 0 listens to symptoms from patient 0
Patient 1 leaves receptionist and sits in waiting room
Patient 0 receives advice from doctor 0
Doctor 2 listens to symptoms from patient 2
Patient 2 receives advice from doctor 2
Nurse 1 takes patient 1 to doctor's office
Patient 1 enters doctor 1's office
Doctor 1 listens to symptoms from patient 1
Patient 1 receives advice from doctor 1
Patient 0 leaves
Patient 2 leaves
Patient 1 leaves
Simulation complete

