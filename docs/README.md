# Interfaces

Additional functional interfaces for the Java Environment.
Includes pre-initialized lambda instances to help re-usage 
and default state inferences.
Also includes 'ToString'-type static utilities and a StackTrace-formatter as part of the ToStringFunction.

This artifact is divided into two sub-packages:

- .interfaces: Which contain the main functional interfaces.
   - [BinaryFunction](javadoc\interfaces\BinaryFunction.html)
   - [BinaryPredicate](javadoc\interfaces\BinaryPredicate.html)
   - [BinaryBooleanOperator](javadoc\interfaces\BinaryBooleanOperator.html)
   - [OnSwapped](javadoc\interfaces\OnSwapped.html)
   - [Register](javadoc\interfaces\Register.html)
   - [StringSupplier](javadoc\interfaces\StringSupplier.html)
   - [StringUnaryOperator](javadoc\interfaces\StringUnaryOperator.html)
   - [ToStringFunction](javadoc\interfaces\ToStringFunction.html)
- .utils: Which contain lambda instances that do not necessarily belong to the interfaces provided by this artifact.
   - [Consumers](javadoc\utils\Consumers.html)
   - [Functions](javadoc\utils\Functions.html)