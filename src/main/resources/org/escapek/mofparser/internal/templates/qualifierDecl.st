$header$

Qualifier $qualifierName$ : $dataType$$if(isarray)$${[$array$]}$$endif$ $if(defaultValue)$${= $defaultValue$}$$endif$$if(scopes)$,
	Scope($scopes; separator=", "$)$endif$$if(flavors)$, 
	Flavor($flavors; separator=", "$)$endif$
;