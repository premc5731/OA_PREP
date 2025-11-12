import React, { useState } from 'react';
import QuizSetup from './QuizSetup';
import Quiz from './Quiz';
import Score from './Score';
import LoginScreen from './LoginScreen'; // <-- 1. IMPORT THE NEW COMPONENT
import './Quiz.css'; 

function App() {
  // --- 2. ADD NEW AUTHENTICATION STATE ---
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // --- Your existing states ---
  const [quizState, setQuizState] = useState('setup'); // 'setup', 'active', 'finished'
  const [questions, setQuestions] = useState([]);
  const [score, setScore] = useState(0);

  // --- Your existing startQuiz, finishQuiz, restartQuiz functions are UNCHANGED ---
  
  const startQuiz = async (topic, count) => {
    console.log(`Fetching ${count} questions for topic: ${topic}`);
    
    // This is the API endpoint you specified
    const url = 'http://localhost:8080/api/v1/generate-mcqs'; // or your Nginx/AWS url

    const requestBody = {
      topic: topic,
      count: count
    };

    try {
      const response = await fetch(url, {
        method: 'POST', 
        headers: {
          'Content-Type': 'application/json' 
        },
        body: JSON.stringify(requestBody) 
      });

      if (!response.ok) {
        const errorData = await response.text();
        throw new Error(`Network response was not ok: ${response.status} ${response.statusText} - ${errorData}`);
      }
      
      const data = await response.json();
      
      if (Array.isArray(data)) {
        setQuestions(data);
        setQuizState('active');
      } else {
        throw new Error("API did not return a valid question array.");
      }

    } catch (error) {
      console.error("Failed to fetch questions:", error);
      alert(`Failed to load quiz: ${error.message}`);
    }
  };

  const finishQuiz = (finalScore) => {
    setScore(finalScore);
    setQuizState('finished');
  };

  const restartQuiz = () => {
    setQuizState('setup');
    setQuestions([]);
    setScore(0);
  };

  
  // --- 3. ADD THIS "if" BLOCK ---
  // If the user is NOT authenticated, show the login screen and stop.
  if (!isAuthenticated) {
    // onLoginSuccess is a function we pass to the child.
    // When the child calls it, we update our state.
    return <LoginScreen onLoginSuccess={() => setIsAuthenticated(true)} />;
  }

  // --- 4. YOUR ORIGINAL RETURN ---
  // If we ARE authenticated, this code will run, showing the quiz.
  return (
    <div className="app-container">
      <h1>OA Prep Quiz</h1>
      
      {quizState === 'setup' && (
        <QuizSetup onStartQuiz={startQuiz} />
      )}
      
      {quizState === 'active' && (
        <Quiz questions={questions} onQuizEnd={finishQuiz} />
      )}
      
      {quizState === 'finished' && (
        <Score score={score} total={questions.length} onRestart={restartQuiz} />
      )}
    </div>
  );
}

export default App;