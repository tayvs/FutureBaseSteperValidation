package futureValidator

import scala.concurrent.{ExecutionContext, Future}

class FutureValidationHelper[T](validator: Validator[T]) {

  def validateStep(src: T, acc: T)(implicit ec: ExecutionContext): Future[T] = {
    if (validator.isComplete(src))
      validator.validate(src)
        .transform(validator.concat(acc, _), ex => ValidationStopper.ValidationError(acc, ex.getMessage))
    else Future.failed(ValidationStopper.ValidationStop(acc))
  }

  def validateStep(src: T)(implicit ec: ExecutionContext): Future[T] = {
    if (validator.isComplete(src))
      validator.validate(src)
        .transform({ v => v }, ex => ValidationStopper.ValidationError(src, ex.getMessage))
    else Future.failed(ValidationStopper.ValidationFailed)
  }

}

object FutureValidationHelper {

  implicit def validator2FVHelper[T](validator: Validator[T]): FutureValidationHelper[T] =
    new FutureValidationHelper(validator)

}
