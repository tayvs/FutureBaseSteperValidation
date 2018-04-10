# FutureBasedValidation

Wizard/constructor for Async validation by steps.
Made Async and easier for understanding then it brother (https://github.com/tayvs/ValidationMonad).

Base validation entity (chain) is Validator class, that provide interface for step validation


###Implemented two APIs:

I used simple Campaign class and here it signature:
case class Campaign(one: Option[String] = None, two: Option[String] = None, three: Option[String] = None)

####First, due FutureValidationHelper class and for-comprehension:

    def validate(cmp: Campaign): Future[Campaign] = {
        val res: Future[Campaign] = for {
          oneAcc <- FirstValidator.validateStep(cmp).andThen { case any => println(s"oneACC $any") }
          twoAcc <- SecondValidator.validateStep(cmp, oneAcc).andThen { case any => println(s"twoACC $any") }
          threeAcc <- ThirdValidator.validateStep(cmp, twoAcc).andThen { case any => println(s"threeACC $any") }
        } yield threeAcc
        res.recoverWith { case ValidationStopper.ValidationStop(value: Campaign) => Future.successful(value) }
    }
  
FirstValidator, SecondValidator, ThirdValidator is a instance Validator.
last line with "recoverWith" is necessary because ValidationStop Exception is an Exception-Mark that stop validation 
if step incomplete.

##### second, due FutureValidationStepper class that something like DSL

    import FutureValidationStepper.FutureValidationStepperConstructor
    def validateWithStepper(cmp: Campaign): Future[Campaign] =
        cmp
        .newStep(OneValidator)
        .logStepResult { case any => println(s"oneACC $any") }
        .newStep(SecondValidator)
        .logStepResult { case any => println(s"twoACC $any") }
        .newStep(ThirdValidator)
        .logStepResult { case any => println(s"threeACC $any") }
        .result
      
FirstValidator, SecondValidator, ThirdValidator is a instance Validator.
FutureValidationStepperConstructor is a implicit constructor for DSL because it demand first validation step 
"result" command return Future with result. This Api implementation a tried to good comment

This project is under construct and would has some improving, tests, better documentation.

####Future improvements:
- test (and with dependencies implementations of Validator)
- add for-comprehension to dsl api 
- add information about validation step into Exception-Marks
- m.b. make some refactoring (merge Validator and FutureValidationHelper)
- if merged Validator and FutureValidationHelper then add new ways to build Validator or new types
- add Sync Validator or Wrapper that convert Sync ro Async

leave feedback and advices for improving

P.S. it's my first try to write description of my outer library 
P.P.S. visit "elder brother" of this module by this link https://github.com/tayvs/ValidationMonad