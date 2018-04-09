package futureValidator

import scala.concurrent.{ExecutionContext, Future}
import FutureValidationHelper._

import scala.util.Try

case class FutureValidationStepper[T] private(private val src: T, private val res: Future[T]) {

  def newStep(validationStep: Validator[T])(implicit ec: ExecutionContext): FutureValidationStepper[T] =
    copy(
      res = res.flatMap(r => validationStep.validateStep(src, r))
    )

  def logStepResult(tryLogger: PartialFunction[Try[T], Unit])(implicit ec: ExecutionContext): FutureValidationStepper[T] =
    copy(
      res = res.andThen(tryLogger)
    )

  def result: Future[T] = res

}

object FutureValidationStepper {

  implicit class FutureValidationStepperConstructor[T](src: T) {

    def newStep(validationStep: Validator[T])(implicit ec: ExecutionContext): FutureValidationStepper[T] =
      new FutureValidationStepper(src, validationStep.validateStep(src))

  }

}
