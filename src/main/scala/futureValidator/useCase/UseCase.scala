package futureValidator.useCase

import futureValidator.useCase.validators._
import futureValidator.FutureValidationHelper.validator2FVHelper
import futureValidator.ValidationStopper
import futureValidator.FutureValidationStepper._
import futureValidator.ValidationStopper.ValidationError

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure

object UseCase extends App {
  
  val campaign = Campaign(Some("valid"), Some("valid"), Some("valid"))
  val campaign1 = Campaign(Some("valid"), Some("valid2"), Some("valid"))
  val campaignWithIncompeteSecondStep = Campaign(Some("valid"), None, Some("valid"))
  
  def validate(cmp: Campaign): Future[Campaign] = {
    val res: Future[Campaign] = for {
      oneAcc <- OneValidator.validateStep(cmp).andThen { case any => println(s"oneACC $any") }
      twoAcc <- SecondValidator.validateStep(cmp, oneAcc).andThen { case any => println(s"twoACC $any") }
      threeAcc <- ThirdValidator.validateStep(cmp, twoAcc).andThen { case any => println(s"threeACC $any") }
    } yield threeAcc
    res.recoverWith { case ValidationStopper.ValidationStop(value: Campaign) => Future.successful(value) }
  }
  
  def validateWithStepper(cmp: Campaign): Future[Campaign] =
    cmp
      .newStep(OneValidator)
      .logStepResult { case any => println(s"oneACC $any") }
      .newStep(SecondValidator)
      .logStepResult { case any => println(s"twoACC $any") }
      .newStep(ThirdValidator)
      .logStepResult { case any => println(s"threeACC $any") }
      .result
  
  //  val res: Future[Unit] = validate(campaign1)
  //    .map(println)
  //    .recover { case ex => ex.printStackTrace() }

//  validateWithStepper(campaign)
//    .map(println)
//    .recover { case ex => ex.printStackTrace() }
//
//  Thread.sleep(2000)
//
//  validateWithStepper(campaign1)
//    .map(println)
//    .recover {
//      case ex@ValidationError(src, _) =>
//        println(s"Valid part $src")
//        ex.printStackTrace()
//      case ex => ex.printStackTrace()
//    }
  
  validateWithStepper(campaignWithIncompeteSecondStep)
    .map(println)
    .recover { case any => println(any) }
  
  Thread.sleep(2000)
  
}
