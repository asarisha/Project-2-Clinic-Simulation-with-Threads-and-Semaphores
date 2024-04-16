import java.util.concurrent.Semaphore;

public class Project2 {
	private static int doctorAssignments[];
	private static int patientToRegister;
	private static int patientInOffice[];
	
	private static Semaphore receptionistReady = new Semaphore(0, true);
	private static Semaphore getPatientNumber = new Semaphore(0, true);
	private static Semaphore registerPatient = new Semaphore(0,true);
	private static Semaphore patientLeftReception = new Semaphore(0, true);
	private static Semaphore patientEnteredOffice[];
	private static Semaphore doctorEnteredOffice[];
	private static Semaphore notifyNurse[];
	private static Semaphore notifyDoctor[];
	private static Semaphore nurseReady[];
	private static Semaphore giveSymptoms[];
	private static Semaphore giveAdvice[];
	private static Semaphore officeVacant[];
	
	static class Receptionist implements Runnable {
        public void run() {
            try {
            	while(true) {
            		//signal that the receptionist is ready and wait for a customer
            		receptionistReady.release();
            		
            		//receive patient number and wait for doctor assignment
            		getPatientNumber.acquire();
            		doctorAssignments[patientToRegister] = (int) (Math.random()*notifyDoctor.length);
            		System.out.println("Receptionist registers patient "+patientToRegister);
            		int temp = patientToRegister;	//save patient ID to notify nurse
            		registerPatient.release();
            		
            		//notify the correct nurse of waiting patient, ensure the nurse has recorded the patient number
            		notifyNurse[doctorAssignments[temp]].release();
            		getPatientNumber.acquire();
            		
            		//wait for patient to leave the reception area
            		patientLeftReception.acquire();
            	}
            } catch(InterruptedException e) {}
        }
    }

    static class Doctor implements Runnable {
    	private int docNum;
    	
    	public Doctor(int docNum) { this.docNum = docNum; }
        public void run() {
            try {
            	while(true) {
            		//wait for nurse to tell doctor a patient is waiting in office, then visit patient
            		notifyDoctor[docNum].acquire();
            		doctorEnteredOffice[docNum].release();
            		
            		//wait for patient to list symptoms then advise the patient
            		giveSymptoms[docNum].acquire();
            		System.out.println("Doctor "+docNum+" listens to symptoms from patient "+patientInOffice[docNum]);
            		giveAdvice[docNum].release();
            	}
            } catch(InterruptedException e) {}
        }
    }

    static class Nurse implements Runnable {
    	private int nurseNum;
    	
    	public Nurse(int nurseNum) { this.nurseNum = nurseNum; }
        public void run() {
        	try {
            	while(true) {
            		//wait for a notification from the receptionist that a patient is waiting
            		notifyNurse[nurseNum].acquire();
            		int patient = patientToRegister;
            		getPatientNumber.release();
            		
            		//let patient know that nurse is ready to take them to the office
            		officeVacant[nurseNum].acquire();
            		nurseReady[patient].release();
            		System.out.println("Nurse "+nurseNum+" takes patient "+patient+" to doctor's office");
            		patientEnteredOffice[nurseNum].release();
            		patientInOffice[nurseNum] = patient;
            		
            		//once the patient has entered, notify the doctor that a patient is waiting
            		notifyDoctor[nurseNum].release();
            		
            	}
            } catch(InterruptedException e) {}
        }
    }

    static class Patient implements Runnable {
    	private int patientNum;
    	
    	public Patient(int patientNum) { this.patientNum = patientNum; }
        public void run() {
        	try {
            	//signal that a customer is waiting and wait for receptionist to be available
        		System.out.println("Patient "+patientNum+" enters waiting room, waits for receptionist");
            	receptionistReady.acquire();
            	
            	//provide patient number and wait for receptionist to register patient and assign doctor number, then leave receptionist
            	patientToRegister = patientNum;
            	getPatientNumber.release();
            	registerPatient.acquire();
            	patientLeftReception.release();
            	System.out.println("Patient "+patientNum+" leaves receptionist and sits in waiting room");
            	int myDoc = doctorAssignments[patientNum];
            	
            	//wait for nurse to call and follow nurse to office
            	nurseReady[patientNum].acquire();
            	patientEnteredOffice[myDoc].acquire();
            	System.out.println("Patient "+patientNum+" enters doctor "+myDoc+"'s office");
            	
            	//wait for doctor to arrive then tell the doctor symptoms and receive advice
            	doctorEnteredOffice[myDoc].acquire();
            	giveSymptoms[myDoc].release();
            	giveAdvice[myDoc].acquire();
            	System.out.println("Patient "+patientNum+" receives advice from doctor"+myDoc);
            	
            	//the patient leaves
            	System.out.println("Patient "+patientNum+" leaves");
            	officeVacant[myDoc].release();
            	
            	
            } catch(InterruptedException e) {}
        }
    }
    
    public static void main(String[] args) {
        //get number of doctors and patients from command line
    	int numDoctors = Integer.parseInt(args[0]);
    	int numPatients = Integer.parseInt(args[1]);
    	doctorAssignments = new int[numPatients];
    	patientInOffice = new int[numDoctors];
    	
    	System.out.println("Run with " + numPatients + " patients, " + numDoctors + " nurses, " + numDoctors + " doctors\n");
    	
    	//initialize threads and arrays of semaphores
        Thread receptionist = new Thread(new Receptionist());
        Thread doctors[] = new Thread[numDoctors];
        Thread nurses[] = new Thread[numDoctors];
        Thread patients[] = new Thread[numPatients];
        
        notifyNurse = new Semaphore[numDoctors];
        notifyDoctor = new Semaphore[numDoctors];
        nurseReady = new Semaphore[numPatients];
        officeVacant = new Semaphore[numDoctors];
        patientEnteredOffice = new Semaphore[numDoctors];
        doctorEnteredOffice = new Semaphore[numDoctors];
        giveSymptoms = new Semaphore[numDoctors];
        giveAdvice = new Semaphore[numDoctors];
        
        //start threads
        receptionist.start();
        for(int i = 0; i < numDoctors; i ++) {
        	//initialize arrays of semaphores
        	notifyNurse[i] = new Semaphore(0, true);
        	notifyDoctor[i] = new Semaphore(0, true);
        	officeVacant[i] = new Semaphore(1, true);
        	patientEnteredOffice[i] = new Semaphore(0, true);
        	doctorEnteredOffice[i] = new Semaphore(0, true);
        	giveSymptoms[i] = new Semaphore(0, true);
        	giveAdvice[i] = new Semaphore(0, true);
        	
        	doctors[i] = new Thread(new Doctor(i));
        	nurses[i] = new Thread(new Nurse(i));
        	
        	doctors[i].start();
        	nurses[i].start();
        }
        for(int i = 0; i < numPatients; i ++) {
        	nurseReady[i] = new Semaphore(0, true);
        	patients[i] = new Thread(new Patient(i));
        	patients[i].start();
        }
        //wait for all the patients to leave
        for(int i = 0; i < numPatients; i ++) {
        	try
       	 	{
        		patients[i].join();
       	 	}
       	 	catch (InterruptedException e)
       	 	{
       	 	}
        }
        
        System.out.println("Simulation complete");
        
    }
}
