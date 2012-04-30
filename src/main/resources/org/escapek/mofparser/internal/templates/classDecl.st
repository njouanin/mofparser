$header$

[$qualifiers:{$it.name$ ($it.value$)}; separator=", "$]
class $className$ $if(parentClass)$${: $parentClass$}$$endif$ {
	$properties:{[$it.qualifiers:{q | $q.qname$ ($q.qvalue$)}; separator=", "$]
$it.type$ $it.name$}; separator=";\n"$;
} 