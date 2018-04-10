package futureValidator

import scala.concurrent.{ExecutionContext, Future}
import FutureValidationHelper._

import scala.util.Try

/**
  * Monad Like validation object.
  * Wizard for sequential validation object by steps.
  * Validation not lazy and processed when step added
  *
  * @param src - object for validation
  * @param res - Future with validated object
  *            or validation error with last valid result
  *            or validation failed error if all (first) step failed
  * @tparam T - type of validation object (for type safety)
  */
case class FutureValidationStepper[T] private(private val src: T, private val res: Future[T]) {
  
  /**
    * Add new validation step in the end of validation.
    * Validation not lazy and processed when step added
    *
    * @param validationStep - Validation step
    * @param ec             - Execution context for Future
    * @return validation constructor
    */
  //todo: user can see Stop exception
  def newStep(validationStep: Validator[T])(implicit ec: ExecutionContext): FutureValidationStepper[T] =
    copy(
      res = res.flatMap(resultAcc => validationStep.validateStep(src, resultAcc))
    )
  
  /**
    * Logged step result. Work like [[Future.andThen]]. If [[ValidationStopper.ValidationError]] occur
    * logger would return [[scala.util.Failure]] with this error every next step
    *
    * @param tryLogger - partial function from Try[T] to any value
    * @param ec        - Execution context for Future
    * @tparam U - Type of logger result
    * @return validation constructor
    */
  def logStepResult[U](tryLogger: PartialFunction[Try[T], U])(implicit ec: ExecutionContext)
  : FutureValidationStepper[T] =
    copy(
      res = res.andThen[U](tryLogger)
    )
  
  /**
    * Future with validated object or one of two errors:
    * [[ValidationStopper.ValidationFailed]] thrown when step validation failed
    * [[ValidationStopper.ValidationFailed]] thrown when all validation failed due first validation step failed
    *
    * @return Future with result or thrown error (use recover with match
    *         to [[ValidationStopper.ValidationFailed]] or [[ValidationStopper.ValidationFailed]])
    */
  def result: Future[T] = res
  
}

object FutureValidationStepper {
  
  /**
    * Create validation constructor [[FutureValidationStepper]] from any value with first validation step
    *
    * @param src - object for validation
    * @tparam T - type of validation object
    */
  implicit class FutureValidationStepperConstructor[T](src: T) {
    
    def newStep(validationStep: Validator[T])(implicit ec: ExecutionContext): FutureValidationStepper[T] =
      new FutureValidationStepper(src, validationStep.validateStep(src))
    
  }
  
}
