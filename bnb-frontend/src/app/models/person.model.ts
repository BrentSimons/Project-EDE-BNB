export interface Person {
  id: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string; // Assuming this is in ISO format (e.g., "0001-01-01")
  accountNumber: number;
  contact: Contact;

}

export interface Contact {
  phoneNumber: string;
  emailAddress: string;
  address: string;
}
