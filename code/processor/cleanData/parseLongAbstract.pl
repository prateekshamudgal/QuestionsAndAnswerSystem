# my $file = '';#Hardcoded
my $file = '$ARGV[0]';
open my $info, $file or die "Could not open $file: $!";
$i=0;
$j=0;
$pi = 3.14159265;
$good=0;
while( my $line = <$info>)  {  
	@summary = split("~",$line);
	@num = split(" ",@summary[1]);
	#print @summary[0],"~";
	$words = $#num+1;
	$i++;
	
	if($words < 3000 && $words > 5)
	{
		$j++;
		chomp(@summary[1]);
		print @summary[0],"~";		
		print @summary[1],"~";	
		#print $words,"\t";
		if($words <1201)
		{
			$score = ((($words-5)*100)/(1200-5));
		}
		else
		{
			$score = 0;
		}
		
		if($score < 60 and $score >15)
		{
			$good++;
			# Means most probably a good article
			# As words would be between 720  and 180
			# Hence would want to boost the scores of such articles 
			# more close to 450 (mid of 720 and 180) better the article is
			if($words < 450)
			{
				$tboost = (540 - (450-$words))/270;
				$score = $score * $tboost;
				$sint = (($score - 45)/30)*$pi/2;
				$wt = sin($sint)*0.95;
				$score = 45+30*$wt;
			}
			else
			{
				$tboost = (540 - ($words-450))/270;
				$score = (75-$score) * $tboost;
				$sint = (($score - 45)/30)*$pi/2;
				$wt = sin($sint)*0.95;
				$score = 45+30*$wt;
			}
		}
		if($score >= 60 && $words>= 720)
		{
			
			$sint = (($score - 80)/20)*$pi/2;
			$wt = sin($sint)*0.35;
			$score = 80 + 20*$wt;
			# $score = $score-(20sqrt($wt*$wt);
			$score = 60 - $score*6/10;
		}
		if($score <= 15.0 && $words<= 200)
		{
			
			$score = $score;
			$sint = (($score - 7.5)/7.5)*$pi/2;
			$wt = sin($sint);
			$score = 9+7.5*$wt;
		}
		print $score."\n";
		$avgScr = $avgScr+((($words-5)*100)/(1200-5));
		print "\n";	
		
	}
	if($i%10000==0)
	{
		print STDERR ($avgScr/10000).",";
		print STDERR $j.",";
		print STDERR $i."\n";

		$j=0;
		$avgScr=0;
	}
}
print STDERR "No of Good Articles: ".$good."\n";