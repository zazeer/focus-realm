import 'package:flutter/material.dart';
import 'package:frontend/userapiauth.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({Key? key}) : super(key: key);

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  bool _isLoading = false;

  @override
  void dispose() {
    _usernameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _register() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });

      try {
        // Buat user model dari input
        final user = UserModel(
          username: _usernameController.text.trim(),
          email: _emailController.text.trim(),
          password: _passwordController.text
        );

        // Panggil API register
        final response = await UserApiService.registerUser(user);

        if (response.errorCode == 200) {
          // Register berhasil
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Successfully registered!'),
              backgroundColor: Colors.green,
            ),
          );
          // Kembali ke login page
          Navigator.pop(context);
        } else {
          // Register gagal
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(response.errorMessage ?? 'Registration failed'),
              backgroundColor: Colors.red,
            ),
          );
        }
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Terjadi kesalahan: $e'),
            backgroundColor: Colors.red,
          ),
        );
      } finally {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }
  @override
  @override
  Widget build(BuildContext context){
    return Scaffold(
    appBar: AppBar(title: const Text('Register'), backgroundColor: Colors.green, foregroundColor: Colors.white,),
    body: Padding(
      padding: const EdgeInsets.all(16.0),
      child: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            children: [
              const SizedBox(height: 32),

              Icon(Icons.person
                , size: 100, color: Colors.green),
              const SizedBox(height: 16),

              TextFormField(
                controller : _usernameController,
                decoration: const InputDecoration(
                  labelText: 'Username',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.person),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty){
                    return "Username is required";
                  }
                  if (value.length < 3) {
                    return 'Username must be at least 3 characters';
                  }
                  return null;
                }
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller : _emailController,
                decoration: const InputDecoration(
                  labelText: 'Email',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.mail),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty){
                    return "Email is required";
                  }
                  if (!value.contains('@')) {
                    return 'Email is not valid!';
                  }
                  return null;
                }
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller : _passwordController,
                decoration: const InputDecoration(
                  labelText: 'Password',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.lock),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty){
                    return "Password is required";
                  }
                  if (value.length < 6) {
                    return 'Password must be at least 3 characters';
                  }
                  return null;
                }
              ),
              const SizedBox(height: 30),

              SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: _isLoading ? null : _register,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.green,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 16),
                    ),
                    child: _isLoading
                        ? const CircularProgressIndicator(color: Colors.white)
                        : const Text('Register'),
                  ),
                ),
                const SizedBox(height: 16),
              
              Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text('Already have an account? '),
                    TextButton(
                      onPressed: () {
                        Navigator.pop(context);
                      },
                      child: const Text('Login'),
                    ),
                  ],
                ),
            ],
          ),
        ),
        ),
      ),
    );
  }
}
