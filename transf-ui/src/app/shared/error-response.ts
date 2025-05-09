export interface ErrorResponse {
  status: number;
  message: string;
  error: ErrorObject;
}

interface ErrorObject {
  errorCode: string;
  message: string;
}
